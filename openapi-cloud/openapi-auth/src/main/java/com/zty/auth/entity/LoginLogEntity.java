package com.zty.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zty.auth.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ZhangTianYou
 * @date 2022/10/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableName("login_log")
public class LoginLogEntity extends BaseEntity {

    /**
     * 用户操作类型   0：用户登录   1：用户退出
     */
    private Integer type;
    /**
     * 状态  0：失败    1：成功    2：账号已锁定
     */
    private Integer status;
    /**
     * 操作ip地址
     */
    private String loginIp;
    /**
     * 用户代理
     */
    private String userAgent;
    /**
     * 用户名
     */
    private String username;

}
