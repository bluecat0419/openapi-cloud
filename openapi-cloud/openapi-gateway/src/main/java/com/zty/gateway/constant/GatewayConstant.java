package com.zty.gateway.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 网关常量定义
 * @author ZhangTianYou
 */
public class GatewayConstant {

    /**
     * 白名单接口
     */
    public static final List<String> WHITE_URL_LIST = Arrays.asList(
            "/cloud/auth-service/login",
            "/cloud/auth-service/backLogin",
            "/cloud/auth-service/register",
            "/cloud/auth-service/code/captcha",
            "/cloud/pay-service/alipay/notifyUrl",
            "/cloud/interface-service/interfaces/homepage"
    );

    /**
     * 时间戳过期时间 30 秒,防止请求重放攻击
     */
    public static final Long TIMESTAMP_EXPIRE_TIME = 30 * 1L;

}
