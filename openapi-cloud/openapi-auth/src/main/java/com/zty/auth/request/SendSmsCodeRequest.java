package com.zty.auth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 发送短信验证码请求参数
 * @author ZhangTianYou
 */
@Data
@ApiModel(value = "发送短信验证码请求参数")
public class SendSmsCodeRequest {

    @Size(min = 11,max = 11,message = "手机号长度必须为11位")
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "类型  0：登录  1：注册")
    @NotNull(message = "类型不能为空")
    private Integer type;

}
