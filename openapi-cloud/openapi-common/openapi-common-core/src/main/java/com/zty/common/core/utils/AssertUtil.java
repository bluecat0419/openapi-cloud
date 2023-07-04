package com.zty.common.core.utils;

import com.zty.common.core.exception.BusinessException;

/**
 * 断言工具类
 * @author ZhangTianYou
 */
public class AssertUtil {

    public static void isTrue(boolean flag,String message){
        if(flag){
            throw new BusinessException(message);
        }
    }

    public static void isTrue(boolean flag,Integer code,String message){
        if(flag){
            throw new BusinessException(code,message);
        }
    }

}
