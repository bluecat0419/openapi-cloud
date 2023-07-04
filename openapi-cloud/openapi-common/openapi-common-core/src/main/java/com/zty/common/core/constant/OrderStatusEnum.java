package com.zty.common.core.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单状态枚举
 * @author ZhangTianYou
 */
public enum OrderStatusEnum {
    SUBMIT("订单已提交",0),
    SUCCESS("支付成功",1),
    TIMEOUT("订单已过期",2),
    REFUND("订单已退款",-1)
    ;

    private final String text;

    private final int value;

    OrderStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
