package com.zty.auth.service;

import com.zty.auth.request.SendEmailCodeRequest;
import com.zty.auth.request.SendSmsCodeRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ZhangTianYou
 */
public interface CodeService {

    /**
     * 获取图形验证码
     * @param response
     * @param uuid
     */
    void captcha(HttpServletResponse response, String uuid);

    /**
     * 发送 登录/注册 短信验证码
     * @param request
     * @return
     */
    Boolean sendSmsCode(SendSmsCodeRequest request);

    /**
     * 发送 登录/注册 邮箱验证码
     * @param request
     * @return
     */
    Boolean sendLoginCodeEmail(SendEmailCodeRequest request);

    /**
     * 发送 生成accessKey/查看secretKey 短信验证码
     * @param type
     * @return
     */
    Boolean sendAccessKeySmsCode(Integer type);

    /**
     * 发送 生成accessKey/查看secretKey 邮箱验证码
     * @param type
     * @return
     */
    Boolean sendAccessKeyEmailCode(Integer type);

}
