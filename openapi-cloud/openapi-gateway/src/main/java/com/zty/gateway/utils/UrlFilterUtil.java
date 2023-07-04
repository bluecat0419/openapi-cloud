package com.zty.gateway.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * url 过滤工具类
 * @author washingtin
 * {@link <a href="https://blog.csdn.net/washingtin/article/details/102976660?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522168428730316800213072816%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=168428730316800213072816&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-3-102976660-null-null.142^v87^control_2,239^v2^insert_chatgpt&utm_term=java%E8%B7%AF%E5%BE%84url%E5%8C%B9%E9%85%8D%E8%BF%87%E6%BB%A4%E5%99%A8&spm=1018.2226.3001.4187">...</a>}
 */
public class UrlFilterUtil {

    /**
     * 将通配符表达式转化为正则表达式
     *
     * @param path
     * @return
     */
    public static String getRegPath(String path) {
        char[] chars = path.toCharArray();
        int len = chars.length;
        StringBuilder sb = new StringBuilder();
        boolean preX = false;
        for (int i = 0; i < len; i++) {
            if (chars[i] == '*') {//遇到*字符
                if (preX) {//如果是第二次遇到*，则将**替换成.*
                    sb.append(".*");
                    preX = false;
                } else if (i + 1 == len) {//如果是遇到单星，且单星是最后一个字符，则直接将*转成[^/]*
                    sb.append("[^/]*");
                } else {//否则单星后面还有字符，则不做任何动作，下一把再做动作
                    preX = true;
                    continue;
                }
            } else {//遇到非*字符
                if (preX) {//如果上一把是*，则先把上一把的*对应的[^/]*添进来
                    sb.append("[^/]*");
                    preX = false;
                }
                if (chars[i] == '?') {//接着判断当前字符是不是?，是的话替换成.
                    sb.append('.');
                } else {//不是?的话，则就是普通字符，直接添进来
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 通配符模式
     *
     * @param excludePath - 不过滤地址
     * @param reqUrl      - 请求地址
     * @return
     */
    public static boolean filterUrls(String excludePath, String reqUrl) {
        String regPath = getRegPath(excludePath);
        return Pattern.compile(regPath).matcher(reqUrl).matches();
    }

    /**
     * 检验是否在非过滤地址
     *
     * @param excludeUrls
     * @param reqUrl
     * @return
     */
    public static boolean checkWhiteList(List<String> excludeUrls, String reqUrl) {
        for (String url : excludeUrls) {
            if (filterUrls(url, reqUrl)) {
                return true;
            }
        }
        return false;
    }

}
