package com.zty.common.core.constant;

/**
 * 常量
 * @author ZhangTianYou
 */
public interface Constant {

    /**
     * token 名称
     */
    String TOKEN_HEADER = "token";

    /**
     * JSON 格式
     */
    String APPLICATION_JSON = "application/json;charset=utf-8";

    /**
     * 当前页码
     */
    String PAGE = "current";

    /**
     * 每页显示条数
     */
    String LIMIT = "limit";

    /**
     * 最大每页显示条数
     */
    Integer MAX_LIMIT = 50;

    /**
     * RSA 公钥
     */
    String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmnG5DPK50ymQBxPDy9ZR1u/9iW/fDsiyArlJ0yTElZ/q0BHqHPP4cU5XcR9ro+YzCGDHCrVoqN9qC2jNEQ2+J44DnyMS2z1pvX5YgxjC0Ew0R2sMByvay4FbKkDxdNcUAPpzRaKaQG1pYv2s1h63P76OxaWDoPp68qn1yaHO9v0KNtiaRG1foN06zxbcyut/Psb1UIhj2JW7KcXA8PcHdx9pLNEWSitxviwlvBJy7kTqZpGe+ojRW0HI+4XBV3CwUyC5gPaTtYSdUvTrlJOwxp7Ok2Rq1LGjOQ9TrGb4GG5db7r8Y3PVx8KxMJ7Wr1udMWkBbcQB1g5zpwfR58Te+wIDAQAB";

    /**
     * 登录类型和状态枚举
     */
    enum Login{
        type_login(0,"用户登录"),
        type_logout(1,"用户退出"),
        status_fail(0,"失败"),
        status_success(1,"成功"),
        status_locked(2,"账号已锁定")
        ;

        private int value;

        private String desc;

        public int getValue() {
            return value;
        }

        public String getDesc(){
            return desc;
        }

        Login(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

    /**
     * 用户状态枚举
     */
    enum UserStatus{
        /**
         * 锁定
         */
        LOCKED(0,"停用"),
        /**
         * 正常
         */
        NORMAL(1,"正常");

        private int value;

        private String desc;

        public int getValue() {
            return value;
        }

        public String getDesc(){
            return desc;
        }

        UserStatus(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

    /**
     * 角色
     */
    interface Role{
        /**
         * 超级管理员状态值
         */
        Integer SUPER_ADMIN_STATUS=1;
        /**
         * 超级管理员名称
         */
        String SUPER_ADMIN_VALUE="super-admin";

    }

    /**
     * 邮件类型
     */
    enum EmailType{
        /**
         * 验证码
         */
        CODE,
        /**
         * 自定义
         */
        CUSTOM
    }

    /**
     * 短信类型
     */
    enum SmsType{
        /**
         * 阿里云模板
         */
        ALIYUN,
        /**
         * 自定义
         */
        CUSTOM
    }

}
