package com.zty.mvcconfig.context;


import com.zty.common.core.vo.UserDetails;

/**
 * 当前登录用户信息
 * @author ZhangTianYou
 */
public class UserContext {

    private static final ThreadLocal<UserDetails> LOGIN_USER_INFO_THREAD_LOCAL=new ThreadLocal<>();

    public static UserDetails getUserInfo(){
        return LOGIN_USER_INFO_THREAD_LOCAL.get();
    }

    public static void setUserInfo(UserDetails loginUserInfo){
        LOGIN_USER_INFO_THREAD_LOCAL.set(loginUserInfo);
    }

    public static Long getUserId(){
        return LOGIN_USER_INFO_THREAD_LOCAL.get() == null ? null : LOGIN_USER_INFO_THREAD_LOCAL.get().getId();
    }

    public static void clear(){
        LOGIN_USER_INFO_THREAD_LOCAL.remove();
    }

}
