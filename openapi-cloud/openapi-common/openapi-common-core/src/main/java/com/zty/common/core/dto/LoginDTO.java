package com.zty.common.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录对象
 * @author ZhangTianYou
 * @date 2022/10/26
 */
@Data
@ApiModel("登录对象")
public class LoginDTO {

    /**
     * 登录类型     0:账号密码登录    1:手机号登录     2:邮箱登录
     */
    @ApiModelProperty("登录类型 0:账号密码登录 1:手机号登录 2:邮箱登录")
    private Integer loginType;

    /**
     * 是否记住我
     */
    private Boolean isRememberMe;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 手机/邮箱验证码
     */
    @ApiModelProperty("手机/邮箱验证码")
    private String code;

    /**
     * 图形验证码
     */
    @ApiModelProperty("图形验证码")
    private String captcha;

    /**
     * uuid 用于唯一标识
     */
    @ApiModelProperty("uuid 用于唯一标识")
    private String uuid;

}
