package com.zty.mvcconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验
 * @author ZhangTianYou
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 必须有某个角色(待扩展)
     * @return
     */
    String mystRole() default "";

    /**
     * 必须是管理员
     * @return
     */
    boolean mustAdmin() default true;

}
