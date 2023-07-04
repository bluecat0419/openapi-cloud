package com.zty.common.core.utils;

/**
 * 脱敏工具类
 * @author ZhangTianYou
 */
public class DesensitizeUtil {

    /**
     * 数据脱敏
     * @param str   要脱敏的字符串
     * @param start 从头数第几位开始脱敏
     * @param end   从结尾数第几位开始脱敏
     * @return
     */
    public static String desensitize(String str,int start,int end){
        if(str == null){
            return null;
        }

        char[] chars = str.toCharArray();
        if(start > chars.length || end > chars.length){
            return str;
        }
        if(start > (chars.length-end)){
            return str;
        }

        for (int i = start; i < (chars.length-end); i++) {
            chars[i] = '*';
        }

        return new String(chars);
    }

}
