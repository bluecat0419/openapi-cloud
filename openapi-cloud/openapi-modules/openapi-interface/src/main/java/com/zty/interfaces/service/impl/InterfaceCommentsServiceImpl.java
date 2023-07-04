package com.zty.interfaces.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zty.common.core.constant.IsDeletedEnum;
import com.zty.common.core.page.PageData;
import com.zty.common.core.utils.AssertUtil;
import com.zty.common.core.utils.ConvertUtil;
import com.zty.common.core.utils.DesensitizeUtil;
import com.zty.common.core.vo.UserDetails;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.interfaces.common.utils.PageUtil;
import com.zty.interfaces.entity.InterfaceCommentsEntity;
import com.zty.interfaces.entity.UserInterfaceInfoEntity;
import com.zty.interfaces.mapper.InterfaceCommentsMapper;
import com.zty.interfaces.request.CommentRequest;
import com.zty.interfaces.service.InterfaceCommentsService;
import com.zty.interfaces.service.UserInterfaceInfoService;
import com.zty.interfaces.vo.InterfaceCommentVO;
import com.zty.mvcconfig.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 接口评论
 * @author ZhangTianYou
 */
@Slf4j
@Service
public class InterfaceCommentsServiceImpl extends ServiceImpl<InterfaceCommentsMapper, InterfaceCommentsEntity> implements InterfaceCommentsService {

    @Resource
    UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    Executor executor;

    @Override
    public boolean checkComment(Long userId, Long interfaceInfoId) {
        LambdaQueryWrapper<InterfaceCommentsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceCommentsEntity::getUserId,userId)
                .eq(InterfaceCommentsEntity::getInterfaceInfoId,interfaceInfoId);
        Long count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

    @Override
    public Long comment(CommentRequest request) {
        UserDetails user = UserContext.getUserInfo();

        UserInterfaceInfoEntity info = userInterfaceInfoService.selectByUserIdAndInterfaceId(user.getId(), request.getInterfaceInfoId());
        AssertUtil.isTrue(info == null,"请先购买后再进行评论");

        boolean flag = checkComment(user.getId(), request.getInterfaceInfoId());
        AssertUtil.isTrue(flag,"您已经评论过了");

        InterfaceCommentsEntity entity = ConvertUtil.convert(request, InterfaceCommentsEntity.class);
        entity.setUserId(user.getId());
        Double score = request.getScore();
        if(request.getScore() > 5.0){
            score = 5.0;
        }
        if(request.getScore() < 0.0){
            score = 0.0;
        }
        entity.setScore(score);
        entity.setUsername(DesensitizeUtil.desensitize(user.getUsername(),1,1));
        baseMapper.insert(entity);

        //异步更新 redis 接口评分
        CompletableFuture.runAsync(()->{
            //todo 替换成redis分布式锁
            synchronized (request.getInterfaceInfoId().toString().intern()){
                List<InterfaceCommentsEntity> list = baseMapper.selectList(new LambdaQueryWrapper<InterfaceCommentsEntity>()
                        .eq(InterfaceCommentsEntity::getInterfaceInfoId, request.getInterfaceInfoId())
                        .eq(InterfaceCommentsEntity::getIsDeleted, IsDeletedEnum.NOT_DELETED.getValue()));
                //计算平均分
                double sum = 0;
                for (InterfaceCommentsEntity commentsEntity : list) {
                    sum += commentsEntity.getScore();
                }
                redisTemplate.opsForZSet().add(
                        RedisKeys.getApiScoreKey(),
                        request.getInterfaceInfoId().toString(),
                        Double.isNaN(sum/list.size()) ? 0:Math.floor((sum/list.size()) * 10) / 10
                );
            }
        },executor);

        return entity.getId();
    }

    @Override
    public PageData<InterfaceCommentVO> page(Map<String, Object> params) {
        AssertUtil.isTrue(params.get("interfaceInfoId") == null || StringUtils.isBlank(params.get("interfaceInfoId").toString()),"接口ID不能为空");
        Long interfaceInfoId = Long.parseLong(params.get("interfaceInfoId").toString());

        LambdaQueryWrapper<InterfaceCommentsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InterfaceCommentsEntity::getInterfaceInfoId,interfaceInfoId)
                .eq(InterfaceCommentsEntity::getIsDeleted, IsDeletedEnum.NOT_DELETED.getValue())
                .orderByDesc(InterfaceCommentsEntity::getCreateDate);
        IPage<InterfaceCommentsEntity> page = baseMapper.selectPage(PageUtil.getPage(params), wrapper);
        return new PageData<>(ConvertUtil.convert(page.getRecords(), InterfaceCommentVO.class),page.getTotal());
    }

}
