package com.zty.common.redis.keys;

/**
 * redis的key前缀
 * @author 张天佑
 */
public interface RedisConstant {

    /**
     * 系统登录用户key前缀
     */
    String LOGIN_USER="sys:loginUser:";
    /**
     * 短信登录验证码key前缀
     */
    String SMS_LOGIN_CODE="sms:login:code:";
    /**
     * 短信注册验证码key前缀
     */
    String SMS_REGISTER_CODE="sms:register:code:";
    /**
     * 缓存用户key前缀
     */
    String CACHE_USER="cache:user:";
    /**
     * 邮箱登录验证码key前缀
     */
    String EMAIL_LOGIN_CODE="email:login:code:";
    /**
     * 邮箱注册验证码key前缀
     */
    String EMAIL_REGISTER_CODE="email:register:code:";
    /**
     * ip 黑名单
     */
    String IP_BLACK_LIST = "ip:black:list";
    /**
     * 注册赠送接口信息key
     */
    String REGISTER_WELFARE = "register:welfare";

    /**
     * 图形验证码 key
     */
    String CAPTCHA = "captcha:";

    /**
     * 接口总调用次数
     */
    String API_ALL_INVOKE_COUNT = "apiCount:AllCount";

    /**
     * 指定接口调用次数
     */
    String API_INVOKE_COUNT = "apiInvokeCount";

    /**
     * nonce key
     */
    String NONCE = "nonce:";

    /**
     * API成交量
     */
    String API_DEAL_COUNT = "apiDealCount";

    /**
     * API评分
     */
    String API_SCORE = "apiScore";

    /**
     * API价格
     */
    String API_SHOW_PRICE = "api:showPrice:";

    /**
     * 生成 accessKey 验证码key前缀
     */
    String EMAIL_GENACCESSKEY_CODE = "email:genAccessKey:code:";

    /**
     * 生成 accessKey 验证码key前缀
     */
    String EMAIL_GETSECRETKEY_CODE = "email:getSecretKey:code:";

    /**
     * 生成 accessKey 验证码key前缀
     */
    String SMS_GENACCESSKEY_CODE = "sms:genAccessKey:code:";

    /**
     * 生成 accessKey 验证码key前缀
     */
    String SMS_GETSECRETKEY_CODE = "sms:getSecretKey:code:";

}
