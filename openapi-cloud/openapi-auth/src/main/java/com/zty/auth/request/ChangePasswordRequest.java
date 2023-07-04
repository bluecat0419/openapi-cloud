package com.zty.auth.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 修改密码方法 入参
 * @author ZhangTianYou
 */
@Data
@ApiModel("修改密码方法 入参")
public class ChangePasswordRequest {

    @ApiModelProperty(value = "旧密码")
    @Size(min = 5,max = 20,message = "密码长度必须在5-20位之间")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @Size(min = 5,max = 20,message = "密码长度必须在5-20位之间")
    private String newPassword;

}
