package com.zty.common.core.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 生成随机验证码工具类
 * @author ZhangTianYou
 */
public class CodeUtils {

    /**
     * 数字
     */
    private static final String SYMBOLS = "0123456789";

    /**
     * 字符串
     */
    // private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    /**
     * 默认获取长度为 6 的随机数字
     * @return 随机数字
     */
    public static String getCode() {
        char[] nonceChars = new char[6];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * 获取指定长度随机数字
     * @return 随机数字
     */
    public static String getCode(int size) {
        char[] nonceChars = new char[size];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

}
