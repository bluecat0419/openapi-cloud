package com.zty.common.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后台登录对象
 */
@Data
@ApiModel("后台登录对象")
public class BackLoginDTO {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("图形验证码")
    private String captcha;

    @ApiModelProperty("uuid 用于唯一标识")
    private String uuid;

}
