package com.zty.auth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 发送邮箱验证码请求参数
 * @author ZhangTianYou
 */
@Data
@ApiModel(value = "发送邮箱验证码请求参数")
public class SendEmailCodeRequest {

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "类型  0：登录  1：注册")
    @NotNull(message = "类型不能为空")
    private Integer type;
}
