package com.zty.openapi.sdk.common;

import java.util.HashMap;

/**
 * 公共模型
 * @author ZhangTianYou
 */
public abstract class BaseModel {

    protected abstract void toMap(HashMap<String, String> var1);

    protected <V> void setParamSimple(HashMap<String, String> map, String key, V value) {
        if (value != null) {
            key = key.replace("_", ".");
            map.put(key, String.valueOf(value));
        }
    }

}
