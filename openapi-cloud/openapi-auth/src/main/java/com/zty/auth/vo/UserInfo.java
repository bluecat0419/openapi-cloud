package com.zty.auth.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.catalina.LifecycleState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户信息
 * 用于前端展示
 * @author ZhangTianYou
 */
@Data
@ApiModel(value = "用户信息对象")
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String username;
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
    @ApiModelProperty(value = "角色列表")
    private List<String> roles;
    @ApiModelProperty(value = "accessKey")
    private String accessKey;
}
