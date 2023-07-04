package com.zty.common.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 注册对象
 * @author ZhangTianYou
 */
@Data
@ApiModel("注册对象")
public class RegisterDTO {

    /**
     * 注册类型     0:账号密码注册    1:手机号注册     2:邮箱注册
     */
    @ApiModelProperty("注册类型 0:账号密码注册 1:手机号注册 2:邮箱注册")
    private Integer registerType;

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
     * 验证码
     */
    @ApiModelProperty("验证码")
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
