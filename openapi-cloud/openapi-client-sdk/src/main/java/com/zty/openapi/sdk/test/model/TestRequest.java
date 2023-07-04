package com.zty.openapi.sdk.test.model;

import com.zty.openapi.sdk.common.BaseModel;

import java.util.HashMap;

/**
 * 测试API请求参数
 * @author ZhangTianYou
 */
public class TestRequest extends BaseModel {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected void toMap(HashMap<String, String> map) {
        this.setParamSimple(map,"content",this.content);
    }

}
