package com.zty.interfaces.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zty.common.core.constant.IsDeletedEnum;
import com.zty.common.core.constant.OrderStatusEnum;
import com.zty.common.core.page.PageData;
import com.zty.common.core.utils.*;
import com.zty.common.core.vo.UserDetails;
import com.zty.common.dubbo.dto.OrderRefundDubboDTO;
import com.zty.common.dubbo.dto.QRCodePayDubboDTO;
import com.zty.common.dubbo.provider.InterfaceProvider;
import com.zty.common.dubbo.provider.PayProvider;
import com.zty.common.dubbo.provider.UserProvider;
import com.zty.common.dubbo.response.OrderRefundResponse;
import com.zty.common.rabbitmq.constant.RabbitConstant;
import com.zty.interfaces.common.utils.PageUtil;
import com.zty.interfaces.entity.InterfaceInfoEntity;
import com.zty.interfaces.entity.InterfaceOrdersEntity;
import com.zty.interfaces.entity.InterfacePackageEntity;
import com.zty.interfaces.entity.UserInterfaceInfoEntity;
import com.zty.interfaces.mapper.InterfaceInfoMapper;
import com.zty.interfaces.mapper.InterfaceOrdersMapper;
import com.zty.interfaces.mapper.UserInterfaceInfoMapper;
import com.zty.interfaces.request.CreateOrderRequest;
import com.zty.interfaces.request.GenerateOrderRequest;
import com.zty.interfaces.request.OrderRefundRequest;
import com.zty.interfaces.service.InterfaceOrdersService;
import com.zty.interfaces.service.InterfacePackageService;
import com.zty.interfaces.vo.GenerateOrderInfoVO;
import com.zty.interfaces.vo.InterfaceOrderDetailsVO;
import com.zty.interfaces.vo.MyOrderDetailVO;
import com.zty.interfaces.vo.MyOrderPageVO;
import com.zty.mvcconfig.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 接口订单
 * @Author ZhangTianYou
 */
@Slf4j
@Service
public class InterfaceOrdersServiceImpl extends ServiceImpl<InterfaceOrdersMapper, InterfaceOrdersEntity> implements InterfaceOrdersService {
    @Resource
    InterfaceInfoMapper interfaceInfoMapper;
    @Resource
    InterfacePackageService interfacePackageService;
    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;
    @DubboReference(timeout = 60000,retries=0)
    PayProvider payProvider;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    InterfaceProvider interfaceProvider;
    @DubboReference(timeout = 60000,retries=0)
    UserProvider userProvider;


    /**
     * 构建查询条件
     * @param params
     * @return
     */
    private LambdaQueryWrapper<InterfaceOrdersEntity> buildWrapper(Map<String,Object> params){
        //订单号
        String orderNo = (String) params.get("orderNo");
        //交易号
        String tradeNo = (String) params.get("tradeNo");
        //支付类型
        Object payType = params.get("payType");
        //状态
        Object status = params.get("status");
        //支付开始时间
        String paymentStartDate = (String) params.get("paymentStartDate");
        //支付结束时间
        String paymentEndDate = (String) params.get("paymentEndDate");

        LambdaQueryWrapper<InterfaceOrdersEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceOrdersEntity::getIsDeleted, IsDeletedEnum.NOT_DELETED.getValue())
                .eq(StringUtils.isNotBlank(orderNo),InterfaceOrdersEntity::getOrderNo,orderNo)
                .eq(StringUtils.isNotBlank(tradeNo),InterfaceOrdersEntity::getTradeNo,tradeNo)
                .between(StringUtils.isNoneBlank(paymentStartDate,paymentEndDate)
                        , InterfaceOrdersEntity::getPaymentDate,paymentStartDate,paymentEndDate)
                .orderByDesc(InterfaceOrdersEntity::getCreateDate);
        if(payType != null){
            wrapper.eq(InterfaceOrdersEntity::getPayType,Integer.parseInt(payType.toString()));
        }
        if(status != null){
            wrapper.eq(InterfaceOrdersEntity::getStatus,Integer.parseInt(status.toString()));
        }

        return wrapper;
    }

    @Override
    public PageData<InterfaceOrdersEntity> page(Map<String, Object> params) {
        ValidatedUtil.checkLimit(params);

        IPage<InterfaceOrdersEntity> page = baseMapper.selectPage(PageUtil.getPage(params), buildWrapper(params));
        return new PageData<>(page.getRecords(),page.getTotal());
    }

    @Override
    public InterfaceOrderDetailsVO get(Long id) {
        InterfaceOrdersEntity entity = this.getById(id);
        InterfaceOrderDetailsVO detailsVO = ConvertUtil.convert(entity, InterfaceOrderDetailsVO.class);
        detailsVO.setUsername(userProvider.selectById(entity.getUserId()).getUsername());
        return detailsVO;
    }

    @Override
    public boolean fakeDelete(Long id) {
        LambdaUpdateWrapper<InterfaceOrdersEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InterfaceOrdersEntity::getId,id)
                .set(InterfaceOrdersEntity::getIsDeleted,IsDeletedEnum.DELETED.getValue());
        return this.update(wrapper);
    }

    @Override
    public PageData<MyOrderPageVO> myOrderPage(Map<String, Object> params) {
        ValidatedUtil.checkLimit(params);
        UserDetails user = UserContext.getUserInfo();

        LambdaQueryWrapper<InterfaceOrdersEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceOrdersEntity::getUserId,user.getId())
                .eq(InterfaceOrdersEntity::getIsDeleted, IsDeletedEnum.NOT_DELETED.getValue())
                .orderByDesc(InterfaceOrdersEntity::getCreateDate);
        IPage<InterfaceOrdersEntity> page = baseMapper.selectPage(PageUtil.getPage(params), wrapper);

        List<MyOrderPageVO> data = page.getRecords().stream().map(item -> {
            InterfaceInfoEntity interfaceInfo = interfaceInfoMapper.selectByPackageId(item.getInterfacePackageId());
            InterfacePackageEntity packageInfo = JSON.parseObject(item.getPackageInfo(), InterfacePackageEntity.class);
            MyOrderPageVO vo = new MyOrderPageVO();
            vo.setOrderId(item.getId());
            vo.setOrderNo(item.getOrderNo());
            vo.setInterfaceName(interfaceInfo.getName());
            vo.setIcon(interfaceInfo.getIcon());
            vo.setCount(item.getCount());
            vo.setPackageName(packageInfo.getName());
            vo.setPrice(item.getTotalPrice());
            vo.setStatus(item.getStatus());
            vo.setPaymentDate(item.getPaymentDate());
            return vo;
        }).collect(Collectors.toList());
        return new PageData<>(data,page.getTotal());
    }

    @Override
    public MyOrderDetailVO myOrderDetail(Long orderId) {
        UserDetails user = UserContext.getUserInfo();

        LambdaQueryWrapper<InterfaceOrdersEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceOrdersEntity::getId,orderId)
                .eq(InterfaceOrdersEntity::getIsDeleted, IsDeletedEnum.NOT_DELETED.getValue());
        InterfaceOrdersEntity order = baseMapper.selectOne(wrapper);
        AssertUtil.isTrue(order == null,"订单不存在");
        AssertUtil.isTrue(!order.getUserId().equals(user.getId()),"只能查看自己的订单");

        InterfacePackageEntity packageInfo = JSON.parseObject(order.getPackageInfo(), InterfacePackageEntity.class);
        InterfaceInfoEntity interfaceInfo = interfaceInfoMapper.selectByPackageId(order.getInterfacePackageId());

        MyOrderDetailVO detailVO = new MyOrderDetailVO();
        detailVO.setInterfaceName(interfaceInfo.getName());
        detailVO.setIcon(interfaceInfo.getIcon());
        detailVO.setOrderId(order.getId());
        detailVO.setOrderNo(order.getOrderNo());
        detailVO.setTradeNo(order.getTradeNo());
        detailVO.setPackageName(packageInfo.getName());
        detailVO.setPrice(packageInfo.getPrice());
        detailVO.setDiscount(packageInfo.getDiscount());
        detailVO.setRealPrice(order.getTotalPrice());
        detailVO.setCount(order.getCount());
        detailVO.setCreateDate(order.getCreateDate());
        detailVO.setPaymentDate(order.getPaymentDate());
        detailVO.setStatus(order.getStatus());
        return detailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(CreateOrderRequest request) {
        UserDetails user = UserContext.getUserInfo();

        //创建订单
        String outTradeNo = RandomUtil.getOutTradeNo();
        InterfaceOrdersEntity order = new InterfaceOrdersEntity();
        order.setOrderNo(outTradeNo);
        order.setUserId(user.getId());
        order.setPayType(request.getPayType());
        order.setCount(request.getCount());
        order.setInterfacePackageId(request.getInterfacePackageId());
        //计算总金额
        InterfacePackageEntity packageEntity = interfacePackageService.getById(request.getInterfacePackageId());
        Double total = packageEntity.getPrice() * request.getCount();
        Double discount = packageEntity.getDiscount() == null ? 1.0:packageEntity.getDiscount() / 10.0;
        Double totalAmount = new BigDecimal(total * discount).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        order.setTotalPrice(totalAmount);
        order.setPackageInfo(JSON.toJSONString(packageEntity));

        //如果金额为0,则直接返回支付成功
        if(totalAmount <= 0){
            this.save(order);
            //支付成功
            Map<String,String> param = new HashMap<>();
            param.put("out_trade_no",outTradeNo);
            interfaceProvider.orderSuccess(param);
            return outTradeNo;
        }

        //调用支付宝创建订单并获取二维码码串
        QRCodePayDubboDTO payDubboDTO = new QRCodePayDubboDTO();
        payDubboDTO.setOrderNo(outTradeNo);
        payDubboDTO.setSubject(packageEntity.getName());
        payDubboDTO.setTotalAmount(totalAmount.toString());
        //String qrCode = payProvider.AliQRCodePay(payDubboDTO);
        //if(StringUtils.isBlank(qrCode)){
        //    throw new BusinessException("生成支付码失败,请尝试重新下单");
        //}
        String qrCode = "123";
        order.setQrCode(qrCode);
        this.save(order);

        //发送消息到订单队列中
        MessageProperties messageProperties=new MessageProperties();
        messageProperties.setCorrelationId(order.getId().toString());
        rabbitTemplate.convertAndSend(
                RabbitConstant.orderExchange,
                RabbitConstant.orderKey,
                new Message(order.getOrderNo().getBytes(),messageProperties)
        );

        return outTradeNo;
    }

    @Override
    public Boolean orderRefund(OrderRefundRequest request) {
        UserDetails user = UserContext.getUserInfo();

        InterfaceOrdersEntity order = this.getById(request.getId());
        AssertUtil.isTrue(order == null,"订单不存在");
        AssertUtil.isTrue(order.getTotalPrice() <= 0,"该订单不符合退款条件");
        AssertUtil.isTrue(order.getStatus() != OrderStatusEnum.SUCCESS.getValue(),"该订单不符合退款条件");
        AssertUtil.isTrue(!order.getUserId().equals(user.getId()),"该订单不符合退款条件");

        //校验是否符合退款条件
        InterfacePackageEntity packageInfo = interfacePackageService.getById(order.getInterfacePackageId());
        UserInterfaceInfoEntity userInterface = userInterfaceInfoMapper.selectByUserIdAndPackageId(user.getId(), order.getInterfacePackageId());
        AssertUtil.isTrue(userInterface == null,"该订单不符合退款条件");
        //判断时间是否已经过去7天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysAgo = calendar.getTime();
        AssertUtil.isTrue(order.getCreateDate().before(sevenDaysAgo),"已超过退款时间");
        //该订单总共调用次数
        Integer count = packageInfo.getInvokeCount() * order.getCount();
        AssertUtil.isTrue(userInterface.getLeftNum() < count,"您的剩余数量不足,无法退款");

        //调用支付宝退款接口
        OrderRefundDubboDTO refundDubboDTO = new OrderRefundDubboDTO();
        refundDubboDTO.setOrderNo(order.getOrderNo());
        refundDubboDTO.setTradeNo(order.getTradeNo());
        refundDubboDTO.setRequestNo(String.valueOf(System.currentTimeMillis()));
        refundDubboDTO.setRefundReason(request.getRefundReason());
        refundDubboDTO.setRefundAmount(String.valueOf(order.getTotalPrice()));
        OrderRefundResponse response = payProvider.orderRefund(refundDubboDTO);

        //退款成功后修改订单状态
        if(response.getIsSuccess()){
            log.info("订单号为[{}]的订单退款成功",order.getOrderNo());
            order.setStatus(OrderStatusEnum.REFUND.getValue());
            order.setRefundInfo(response.getRefundInfo());
            this.updateById(order);
            //修改用户剩余接口次数
            userInterface.setTotalNum(userInterface.getTotalNum() - count);
            userInterface.setLeftNum(userInterface.getLeftNum() - count);
            userInterfaceInfoMapper.updateById(userInterface);
        }else {
            log.error("订单号为[{}]的订单退款失败",order.getOrderNo());
        }
        return response.getIsSuccess();
    }

    @Override
    public Boolean updateOrderStatus(String orderNo, Integer status) {
        LambdaUpdateWrapper<InterfaceOrdersEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InterfaceOrdersEntity::getOrderNo,orderNo)
                .set(InterfaceOrdersEntity::getStatus,status);
        return this.update(wrapper);
    }

    @Override
    public InterfaceOrdersEntity selectByOrderNo(String orderNo) {
        LambdaQueryWrapper<InterfaceOrdersEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceOrdersEntity::getOrderNo,orderNo);
        return this.getOne(wrapper);
    }

    @Override
    public GenerateOrderInfoVO generateOrder(GenerateOrderRequest request) {
        GenerateOrderInfoVO vo = new GenerateOrderInfoVO();
        InterfacePackageEntity packageInfo = interfacePackageService.getById(request.getInterfacePackageId());
        AssertUtil.isTrue(packageInfo == null,"套餐不存在");

        InterfaceInfoEntity interfaceInfo = interfaceInfoMapper.selectByPackageId(request.getInterfacePackageId());
        AssertUtil.isTrue(interfaceInfo == null,"接口不存在");

        vo.setInterfaceName(interfaceInfo.getName());
        vo.setPackageName(packageInfo.getName());
        vo.setInterfacePackageId(packageInfo.getId());
        vo.setDiscount(packageInfo.getDiscount());
        vo.setCount(request.getCount());
        Double total = packageInfo.getPrice() * request.getCount();
        Double discount = packageInfo.getDiscount() == null ? 1.0:packageInfo.getDiscount() / 10.0;
        Double totalAmount = new BigDecimal(total * discount).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        vo.setPrice(totalAmount);
        return vo;
    }

    @Override
    public Boolean checkPay(String orderNo) {
        InterfaceOrdersEntity order = selectByOrderNo(orderNo);
        AssertUtil.isTrue(order == null,"订单不存在");
        return order.getStatus() == OrderStatusEnum.SUCCESS.getValue();
    }

}
