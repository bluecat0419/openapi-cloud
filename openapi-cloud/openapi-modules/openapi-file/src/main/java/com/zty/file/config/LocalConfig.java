package com.zty.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 本地存储配置
 * @author ZhangTianYou
 */
@Component
@RefreshScope
public class LocalConfig {
    private String domain;

    private String localFilePath;

    @Value("${storage.local.file.domain}")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Value("${storage.local.file.path}")
    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getDomain() {
        return domain;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

}
