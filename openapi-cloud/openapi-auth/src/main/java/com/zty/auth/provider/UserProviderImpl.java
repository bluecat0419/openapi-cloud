package com.zty.auth.provider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zty.auth.entity.UserEntity;
import com.zty.auth.mapper.UserMapper;
import com.zty.common.core.utils.ConvertUtil;
import com.zty.common.core.vo.UserDetails;
import com.zty.common.dubbo.provider.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author ZhangTianYou
 */
@Slf4j
@DubboService
public class UserProviderImpl implements UserProvider {

    @Resource
    UserMapper userMapper;


    @Override
    @SentinelResource(value = "selectById",fallbackClass = UserProviderFallback.class,fallback = "selectByIdFallback")
    public UserDetails selectById(Long id) {
        UserEntity entity = userMapper.selectById(id);
        if(entity == null){
            return new UserDetails();
        }
        return ConvertUtil.convert(entity,UserDetails.class);
    }

    @Override
    @SentinelResource(value = "selectByAccessKey",fallbackClass = UserProviderFallback.class,fallback = "selectByAccessKeyFallback")
    public UserDetails selectByAccessKey(String accessKey) {
        UserEntity entity = userMapper.selectByAccessKey(accessKey);
        return ConvertUtil.convert(entity,UserDetails.class);
    }

}
