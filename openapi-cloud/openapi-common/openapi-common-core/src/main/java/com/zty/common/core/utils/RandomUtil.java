package com.zty.common.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RandomUtil {

    /**
     * 生成指定长度随机字符串,大小写字母加数字组合
     * @param inNum     生成长度
     * @return
     */
    public static String randomStr(int inNum){
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < inNum; i++) {
            switch (random.nextInt(3)){
                case 0:
                    char ranLowLetter = (char) ((random.nextInt(26) + 97));
                    str.append(ranLowLetter);
                    break;
                case 1:
                    char ranUpLetter = (char) ((random.nextInt(26) + 65));
                    str.append(ranUpLetter);
                    break;
                case 2:
                    int ranNumOut = random.nextInt(10);
                    str.append(ranNumOut);
                    break;
            }
        }
        return str.toString();
    }

    /**
     * 生成订单号
     * @return
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        key = key + System.currentTimeMillis();
        key = key.substring(0, 15);
        return key;
    }

}
