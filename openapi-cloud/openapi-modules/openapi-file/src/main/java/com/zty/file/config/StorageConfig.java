package com.zty.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 存储配置
 * @author ZhangTianYou
 */
@Component
@RefreshScope
public class StorageConfig {

    private String type;

    @Value("${storage.type}")
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
