package com.zty.common.dubbo.provider;

import com.zty.common.core.vo.UserDetails;

/**
 * 用户服务
 */
public interface UserProvider {

    /**
     * 根据 id 查询用户
     * @param id
     * @return
     */
    UserDetails selectById(Long id);

    /**
     * 根据 accessKey 查询用户
     * @param accessKey
     * @return
     */
    UserDetails selectByAccessKey(String accessKey);

}
