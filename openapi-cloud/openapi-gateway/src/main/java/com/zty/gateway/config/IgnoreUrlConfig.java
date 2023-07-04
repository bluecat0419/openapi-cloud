package com.zty.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 忽略的 URL 配置类
 * @author ZhangTianYou
 */
@RefreshScope
@Component
public class IgnoreUrlConfig {

    private List<String> ignoreUrls;

    @Value("${ignore.urls}")
    public void setIgnoreUrls(String ignoreUrls) {
        this.ignoreUrls = Arrays.asList(ignoreUrls.split(","));
    }

    public List<String> getIgnoreUrls() {
        return this.ignoreUrls;
    }

}
