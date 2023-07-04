package com.zty.auth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 生成 accessKey 请求对象
 * @author ZhangTianYou
 */
@Data
@ApiModel("生成 accessKey 请求对象")
public class GenAccessKeyRequest {

    @ApiModelProperty(value = "校验类型  0:密码  1:手机号  2:邮箱")
    @NotNull(message = "校验类型不能为空")
    private Integer checkType;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;

}
