package com.zty.interfaces.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zty.common.core.constant.IsDeletedEnum;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.interfaces.entity.InterfaceInfoEntity;
import com.zty.interfaces.mapper.InterfaceInfoMapper;
import com.zty.interfaces.service.InterfaceAnalysisService;
import com.zty.interfaces.vo.InvokeCountAnalysisVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口分析
 * @author ZhangTianYou
 */
@Service
public class InterfaceAnalysisServiceImpl implements InterfaceAnalysisService {

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public List<InvokeCountAnalysisVO> invokeCountAnalysis(String topOrTail) {
        LambdaQueryWrapper<InterfaceInfoEntity> wrapper = new LambdaQueryWrapper<InterfaceInfoEntity>()
                .eq(InterfaceInfoEntity::getIsDeleted, IsDeletedEnum.NOT_DELETED);
        List<InterfaceInfoEntity> list = interfaceInfoMapper.selectList(wrapper);
        List<InvokeCountAnalysisVO> data = list.stream().map(item -> {
            //获取接口的调用次数
            Integer invokeCount = Integer.parseInt(redisTemplate.opsForZSet().score(RedisKeys.getApiInvokeCountKey(),item.getId()).toString());
            InvokeCountAnalysisVO vo = new InvokeCountAnalysisVO();
            vo.setId(item.getId());
            vo.setName(item.getName());
            vo.setInvokeCount(invokeCount == null ? 0 : invokeCount);
            return vo;
        }).collect(Collectors.toList());
        List<InvokeCountAnalysisVO> sort;
        if ("top".equals(topOrTail)) {
            sort = data.stream().sorted((o1, o2) -> o2.getInvokeCount() - o1.getInvokeCount()).limit(10).collect(Collectors.toList());
        } else {
            sort = data.stream().sorted((o1, o2) -> o1.getInvokeCount() - o2.getInvokeCount()).limit(10).collect(Collectors.toList());
        }
        return sort;
    }

}
