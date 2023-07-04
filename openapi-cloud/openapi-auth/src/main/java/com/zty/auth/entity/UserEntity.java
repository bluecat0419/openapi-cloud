package com.zty.auth.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zty.auth.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 系统用户
 * @author 张天佑
 */
@Data
@TableName("user")
public class UserEntity extends BaseEntity {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像地址
     */
    private String avaUrl;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 性别  0：男  1：女
     */
    private Integer gender;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 状态 0：停用  1：正常
     */
    private Integer status;
    /**
     * 签名 accessKey
     */
    private String accessKey;
    /**
     * 签名 secretKey
     */
    private String secretKey;
    /**
     * 是否超级管理员   0：否   1：是
     */
    private Integer isSuperAdmin;
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
    /**
     * 最后登录时间
     */
    private Date lastLoginDate;

}
