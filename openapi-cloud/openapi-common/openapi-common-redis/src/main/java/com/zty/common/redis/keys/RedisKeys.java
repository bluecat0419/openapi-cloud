package com.zty.common.redis.keys;

import static com.zty.common.core.constant.Constant.*;
import com.zty.common.core.constant.Constant.EmailType;
import com.zty.common.core.exception.BusinessException;

import static com.zty.common.redis.keys.RedisConstant.*;


/**
 * redis键
 * @author 张天佑
 */
public class RedisKeys {

    /**
     * 系统登录用户key
     * @param id
     * @return
     */
    public static String getLoginUserKey(String id){
        return LOGIN_USER+id;
    }

    /**
     * 短信登录验证码key
     * @param phone
     * @return
     */
    public static String getSmsLoginCodeKey(String phone){
        return SMS_LOGIN_CODE+phone;
    }

    /**
     * 短信注册验证码key
     * @param phone
     * @return
     */
    public static String getSmsRegisterCodeKey(String phone){
        return SMS_REGISTER_CODE+phone;
    }

    /**
     * 邮箱登录验证码key
     * @param email
     * @return
     */
    public static String getEmailLoginCodeKey(String email){
        return EMAIL_LOGIN_CODE+email;
    }

    /**
     * 邮箱注册验证码key
     * @param email
     * @return
     */
    public static String getEmailRegisterCodeKey(String email){
        return EMAIL_REGISTER_CODE+email;
    }

    /**
     * 缓存用户key
     * @param userId    用户id
     * @return
     */
    public static String getCacheUserKey(String userId){
        return CACHE_USER+userId;
    }

    /**
     * 注册赠送接口信息key
     * @return
     */
    public static String getRegisterWelfareKey(){
        return REGISTER_WELFARE;
    }

    /**
     * 图形验证码 key
     * @param uuid      用户唯一标识 uuid
     * @return
     */
    public static String getCaptchaKey(String uuid){
        return CAPTCHA+uuid;
    }

    /**
     * 接口总调用次数
     * @return
     */
    public static String getApiAllInvokeCountKey(){
        return API_ALL_INVOKE_COUNT;
    }

    /**
     * 指定接口调用次数
     * @return
     */
    public static String getApiInvokeCountKey(){
        return API_INVOKE_COUNT;
    }

    /**
     * nonce key
     * @param nonce
     * @return
     */
    public static String getNonceKey(String nonce){
        return NONCE+nonce;
    }

    /**
     * api成交量key
     * @return
     */
    public static String getApiDealCountKey(){
        return API_DEAL_COUNT;
    }

    /**
     * api评分key
     * @return
     */
    public static String getApiScoreKey(){
        return API_SCORE;
    }

    /**
     * 接口套餐key,用于主页分页显示
     * @return
     */
    public static String getApiShowPriceKey(String packageId){
        return API_SHOW_PRICE+packageId;
    }

    /**
     * 生成 accessKey 邮箱验证码 key
     * @param email
     * @return
     */
    public static String getEmailGenAccessKey(String email){
        return EMAIL_GENACCESSKEY_CODE+email;
    }

    /**
     * 获取 SecretKey 邮箱验证码 key
     * @param email
     * @return
     */
    public static String getEmailGetSecretKey(String email){
        return EMAIL_GETSECRETKEY_CODE+email;
    }

    /**
     * 生成 accessKey 短信验证码 key
     * @param sms
     * @return
     */
    public static String getSmsGenAccessKey(String sms){
        return EMAIL_GENACCESSKEY_CODE+sms;
    }

    /**
     * 获取 SecretKey 短信验证码 key
     * @param sms
     * @return
     */
    public static String getSmsGetSecretKey(String sms){
        return EMAIL_GETSECRETKEY_CODE+sms;
    }

}
