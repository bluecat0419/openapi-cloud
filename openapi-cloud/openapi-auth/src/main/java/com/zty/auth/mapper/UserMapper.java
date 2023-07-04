package com.zty.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zty.auth.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 张天佑
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    UserEntity getByUserName(@Param("username") String username);

    /**
     * 根据手机号获取用户
     * @param phone
     * @return
     */
    UserEntity getByPhone(@Param("phone") String phone);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    UserEntity getById(@Param("id") Long id);

    /**
     * 根据邮箱获取用户
     * @param email
     * @return
     */
    UserEntity getByEmail(@Param("email") String email);

    /**
     * 根据 accessKey 查询用户
     * @param accessKey
     * @return
     */
    UserEntity selectByAccessKey(@Param("accessKey") String accessKey);

    /**
     * 校验 accessKey 是否已存在
     * @param accessKey
     * @return
     */
    Integer checkAccessKey(@Param("accessKey") String accessKey);
}
