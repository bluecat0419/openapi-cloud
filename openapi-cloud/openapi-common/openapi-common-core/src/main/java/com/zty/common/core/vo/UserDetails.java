package com.zty.common.core.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户详细信息
 * 用于在服务之间传输
 * @author ZhangTianYou
 */
@Data
public class UserDetails implements Serializable {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 头像地址
     */
    private String avaUrl;
    /**
     * 性别  0：男  1：女
     */
    private Integer gender;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 签名 accessKey
     */
    private String accessKey;

    /**
     * 签名 secretKey
     */
    private String secretKey;
    /**
     * 状态 0：停用  1：正常
     */
    private Integer status;
    /**
     * 是否超级管理员   0：否   1：是
     */
    private Integer isSuperAdmin;
}
