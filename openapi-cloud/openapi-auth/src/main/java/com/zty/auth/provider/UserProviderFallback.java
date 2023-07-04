package com.zty.auth.provider;

import com.zty.common.core.vo.UserDetails;
import lombok.extern.slf4j.Slf4j;

/**
 * 降级处理类
 * @author ZhangTianYou
 */
@Slf4j
public class UserProviderFallback {

    public static UserDetails selectByIdFallback(Long id,Throwable e){
        log.error("触发降级,记录异常日志：{}",e.getMessage());
        return new UserDetails();
    }

    public static UserDetails selectByAccessKeyFallback(String accessKey,Throwable e){
        log.error("触发降级,记录异常日志：{}",e.getMessage());
        return null;
    }

}
