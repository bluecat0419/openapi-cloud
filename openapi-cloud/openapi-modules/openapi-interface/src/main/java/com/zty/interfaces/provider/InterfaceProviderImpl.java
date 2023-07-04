package com.zty.interfaces.provider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zty.common.core.constant.InterfaceStatusEnum;
import com.zty.common.core.constant.IsDeletedEnum;
import com.zty.common.core.constant.OrderStatusEnum;
import com.zty.common.core.utils.ConvertUtil;
import com.zty.common.dubbo.dto.InterfaceInfoDubboDTO;
import com.zty.common.dubbo.provider.InterfaceProvider;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.interfaces.entity.InterfaceInfoEntity;
import com.zty.interfaces.entity.InterfaceOrdersEntity;
import com.zty.interfaces.entity.InterfacePackageEntity;
import com.zty.interfaces.entity.UserInterfaceInfoEntity;
import com.zty.interfaces.mapper.UserInterfaceInfoMapper;
import com.zty.interfaces.request.AddRegisterWelfareRequest;
import com.zty.interfaces.service.InterfaceOrdersService;
import com.zty.interfaces.service.InterfacePackageService;
import com.zty.interfaces.service.InterfaceService;
import com.zty.interfaces.service.UserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 实现提供者
 * @author ZhangTianYou
 */
@Slf4j
@DubboService
public class InterfaceProviderImpl implements InterfaceProvider {
    @Resource
    UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    InterfaceService interfaceService;
    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Resource
    InterfaceOrdersService interfaceOrdersService;
    @Resource
    InterfacePackageService interfacePackageService;
    @Resource
    private PlatformTransactionManager platformTransactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public void registerWelfare(Long userId) {
        //查询现有的新用户注册奖励接口有哪些
        Set members = redisTemplate.opsForSet().members(RedisKeys.getRegisterWelfareKey());
        Iterator var1 = members.iterator();

        while (var1.hasNext()) {
            AddRegisterWelfareRequest next = (AddRegisterWelfareRequest) var1.next();
            userInterfaceInfoService.save(next.toUserInterfaceInfoEntity(userId));
        }
    }

    @Override
    public InterfaceInfoDubboDTO selectByUrlAndMethod(String url, String method) {
        LambdaQueryWrapper<InterfaceInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceInfoEntity::getStatus, InterfaceStatusEnum.ONLINE.getValue())
                .eq(InterfaceInfoEntity::getIsDeleted, IsDeletedEnum.NOT_DELETED.getValue())
                .like(InterfaceInfoEntity::getUrl,url)
                .eq(InterfaceInfoEntity::getMethod,method);
        InterfaceInfoEntity interfaceInfo = interfaceService.getOne(wrapper);
        return ConvertUtil.convert(interfaceInfo,InterfaceInfoDubboDTO.class);
    }

    @Override
    public Boolean checkUserInterface(Long interfaceId, Long userId) {
        Integer flag = userInterfaceInfoMapper.checkUserInterface(interfaceId, userId);
        return flag != null;
    }

    @Override
    public Boolean reduceUserInterfaceInvokeCount(Long interfaceId, Long userId) {
        log.info("[{}]调用了[{}]接口",userId,interfaceId);
        Integer rowCount = userInterfaceInfoMapper.reduceUserInterfaceInvokeCount(interfaceId, userId);
        return rowCount > 0;
    }

    @Override
    @SentinelResource(value = "orderSuccess",fallbackClass = InterfaceProviderFallback.class,fallback = "orderSuccessFallback")
    public Boolean orderSuccess(Map<String,String> params) {
        //订单号
        String orderNo = params.get("out_trade_no");
        //支付宝交易号
        String tradeNo = params.get("trade_no");

        log.info("订单[{}]支付成功,开始处理",orderNo);
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        //查询订单信息
        InterfaceOrdersEntity order = interfaceOrdersService.selectByOrderNo(orderNo);
        if(order == null){
            log.error("订单[{}]不存在",orderNo);
            return false;
        }
        //判断订单是否已经处理过,防止重复处理
        if(order.getStatus() == OrderStatusEnum.SUCCESS.getValue()){
            log.info("订单[{}]已经处理过",orderNo);
            return true;
        }

        //修改订单状态为支付成功
        order.setStatus(OrderStatusEnum.SUCCESS.getValue());
        order.setPayInfo(JSON.toJSONString(params));
        order.setPaymentDate(new Date());
        order.setTradeNo(tradeNo);
        Boolean flag = interfaceOrdersService.updateById(order);
        if(!flag){
            log.error("订单[{}]状态修改失败",orderNo);
            platformTransactionManager.rollback(transaction);
            return false;
        }

        //添加用户接口次数
        InterfacePackageEntity packageInfo = interfacePackageService.getById(order.getInterfacePackageId());
        if(packageInfo == null){
            log.error("订单[{}]对应的套餐不存在",orderNo);
            platformTransactionManager.rollback(transaction);
            return false;
        }

        //增加接口成交数
        redisTemplate.opsForZSet().incrementScore(RedisKeys.getApiDealCountKey(),packageInfo.getInterfaceInfoId().toString(),1);

        //查询用户接口信息
        UserInterfaceInfoEntity userInterfaceInfo = userInterfaceInfoService.selectByUserIdAndInterfaceId(order.getUserId(), packageInfo.getInterfaceInfoId());
        Integer invokeCount = packageInfo.getInvokeCount() * order.getCount();
        if(userInterfaceInfo == null){
            //为空则添加
            userInterfaceInfo = new UserInterfaceInfoEntity();
            userInterfaceInfo.setUserId(order.getUserId());
            userInterfaceInfo.setInterfaceInfoId(packageInfo.getInterfaceInfoId());
            userInterfaceInfo.setTotalNum(invokeCount);
            userInterfaceInfo.setLeftNum(invokeCount);
            boolean save = userInterfaceInfoService.save(userInterfaceInfo);
            if(!save){
                log.error("订单[{}]对应的用户接口信息添加失败",orderNo);
                platformTransactionManager.rollback(transaction);
                return false;
            }
            log.info("订单[{}]处理成功,save",orderNo);
            platformTransactionManager.commit(transaction);
            return true;
        }

        //不为空则修改
        userInterfaceInfo.setTotalNum(userInterfaceInfo.getTotalNum() + invokeCount);
        userInterfaceInfo.setLeftNum(userInterfaceInfo.getLeftNum() + invokeCount);
        boolean update = userInterfaceInfoService.updateById(userInterfaceInfo);
        if(!update){
            log.error("订单[{}]对应的用户接口信息修改失败",orderNo);
            platformTransactionManager.rollback(transaction);
            return false;
        }
        log.info("订单[{}]处理成功,update",orderNo);
        platformTransactionManager.commit(transaction);
        return true;
    }


}
