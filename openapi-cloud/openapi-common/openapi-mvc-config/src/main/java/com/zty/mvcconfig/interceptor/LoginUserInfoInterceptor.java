package com.zty.mvcconfig.interceptor;

import com.zty.common.core.constant.Constant;
import com.zty.common.core.exception.BusinessException;
import com.zty.common.core.utils.AssertUtil;
import com.zty.common.core.utils.TokenParseUtil;
import com.zty.common.core.vo.UserDetails;
import com.zty.mvcconfig.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户身份统一登录拦截
 * @author ZhangTianYou
 */
@Slf4j
@Component
public class LoginUserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (checkWhiteListUrl(request.getRequestURI())) {
            return true;
        }

        String token = request.getHeader(Constant.TOKEN_HEADER);

        UserDetails loginUserInfo;

        try {
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        } catch (Exception e) {
            log.error("token 解析失败,[{}]",e.getMessage(),e);
            throw new BusinessException("令牌解析失败,请尝试重新登录");
        }

        AssertUtil.isTrue(loginUserInfo == null,"请先登录");

        UserContext.setUserInfo(loginUserInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 在请求完全结束后调用，常用于清理资源等工作
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(UserContext.getUserInfo() != null){
            UserContext.clear();
        }
    }

    /**
     * 校验是否是白名单接口
     * @param url
     * @return
     */
    private boolean checkWhiteListUrl(String url){
        return StringUtils.containsAny(url,"springfox","swagger","v2","webjars","doc.html");
    }
}
