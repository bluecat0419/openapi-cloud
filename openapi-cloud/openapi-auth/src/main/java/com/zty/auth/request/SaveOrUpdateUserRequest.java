package com.zty.auth.request;

import com.zty.common.core.groups.SaveGroup;
import com.zty.common.core.groups.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 用户 新增/修改 入参
 * @author ZhangTianYou
 */
@Data
@ApiModel("用户 新增/修改 入参")
public class SaveOrUpdateUserRequest {

    @NotNull(groups = UpdateGroup.class,message = "id 不能为空")
    @Null(groups = SaveGroup.class,message = "id 必须为空")
    private Long id;

    @ApiModelProperty(value = "用户名")
    @Size(groups = {SaveGroup.class,UpdateGroup.class},min = 1,message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    @Size(groups = {SaveGroup.class},min = 1,message = "密码不能为空")
    private String password;

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

    @ApiModelProperty(value = "状态 0：停用  1：正常")
    private Integer status;

}
