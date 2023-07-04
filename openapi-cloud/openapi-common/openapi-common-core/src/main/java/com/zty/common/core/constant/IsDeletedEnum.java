package com.zty.common.core.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 是否删除枚举
 * @author ZhangTianYou
 */
public enum IsDeletedEnum {

    NOT_DELETED("未删", 0),
    DELETED("已删", 1);

    private final String text;

    private final int value;

    IsDeletedEnum(String text, int value) {
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

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

}
