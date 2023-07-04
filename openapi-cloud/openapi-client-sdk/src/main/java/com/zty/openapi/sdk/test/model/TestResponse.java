package com.zty.openapi.sdk.test.model;

import com.zty.openapi.sdk.common.BaseModel;

import java.util.HashMap;

/**
 * 测试API返回参数
 * @author ZhangTianYou
 */
public class TestResponse extends BaseModel {

    /**
     * 请求 ip
     */
    private String ip;

    /**
     * 随机名称
     */
    private String name;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    protected void toMap(HashMap<String, String> map) {
        this.setParamSimple(map,"ip",this.ip);
        this.setParamSimple(map,"name",this.name);
    }
}
