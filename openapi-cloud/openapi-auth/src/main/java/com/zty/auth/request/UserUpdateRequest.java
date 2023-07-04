package com.zty.auth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户修改请求参数
 */
@Data
@ApiModel("用户修改请求参数")
public class UserUpdateRequest {

    @ApiModelProperty(value = "头像地址")
    private String avaUrl;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "性别  0：男  1：女")
    private Integer gender;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话")
    private String mobile;

}
