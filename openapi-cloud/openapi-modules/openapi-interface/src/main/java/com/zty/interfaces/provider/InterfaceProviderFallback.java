package com.zty.interfaces.provider;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 降级处理类
 * @author ZhangTianYou
 */
@Slf4j
public class InterfaceProviderFallback {

    public static Boolean orderSuccessFallback(Map<String,String> params,Throwable e){
        log.error("订单处理失败",e);
        return Boolean.FALSE;
    }

}
