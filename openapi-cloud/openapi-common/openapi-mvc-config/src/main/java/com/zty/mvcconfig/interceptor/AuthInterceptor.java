package com.zty.mvcconfig.interceptor;

import com.zty.common.core.exception.BusinessException;
import com.zty.common.core.vo.UserDetails;
import com.zty.mvcconfig.annotation.AuthCheck;
import com.zty.mvcconfig.context.UserContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 权限校验 AOP
 * @author ZhangTianYou
 */
@Aspect
@Component
public class AuthInterceptor {


    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        boolean mustAdmin = authCheck.mustAdmin();
        if(mustAdmin){
            //当前登录用户
            UserDetails userInfo = UserContext.getUserInfo();
            if(userInfo.getIsSuperAdmin() == 0){
                throw new BusinessException(HttpStatus.FORBIDDEN.value(), "没有操作权限");
            }
        }
        return joinPoint.proceed();
    }

}
