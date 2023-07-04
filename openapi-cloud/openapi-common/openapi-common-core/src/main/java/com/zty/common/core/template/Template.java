package com.zty.common.core.template;

/**
 * 模板
 * @author ZhangTianYou
 */
public class Template {

    /**
     * 邮箱模板
     */
    public static class Email{

        /**
         * 登录验证码模板
         * @return
         */
        public static String loginCodeTemplate(String code){
            return "<p>您好!欢迎使用 openapi 开放平台，您的登录验证码是："+code+"，两分钟内有效，请勿泄露，如非本人操作，请忽略此邮件！</p>";
        }

        /**
         * 登录验证码模板
         * @return
         */
        public static String registerCodeTemplate(String code){
            return "<p>您好!欢迎使用 openapi 开放平台，您的注册验证码是："+code+"，两分钟内有效，请勿泄露，如非本人操作，请忽略此邮件！</p>";
        }

        /**
         * 生成 AccessKey 验证码模板
         * @param code
         * @return
         */
        public static String genGenAccessKeyCodeTemplate(String code){
            return "<p>您好!欢迎使用 openapi 开放平台，您正在生成 AccessKey, 验证码是："+code+"，两分钟内有效，请勿泄露，如非本人操作，请忽略此邮件！</p>";
        }

        /**
         * 获取 SecretKey 验证码模板
         * @param secretKey
         * @return
         */
        public static String getSecretKeyCodeTemplate(String secretKey){
            return "<p>您好!欢迎使用 openapi 开放平台，您正在获取 SecretKey, 验证码是："+secretKey+"，两分钟内有效，请勿泄露，如非本人操作，请忽略此邮件！</p>";
        }

    }

    /**
     * 短信模板
     */
    public static class Sms{



    }

}
