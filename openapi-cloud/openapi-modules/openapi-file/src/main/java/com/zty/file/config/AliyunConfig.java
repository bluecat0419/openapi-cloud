package com.zty.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 阿里云存储配置
 * @author ZhangTianYou
 */
@Component
@RefreshScope
public class AliyunConfig {

    /**
     * 桶名称
     */
    private String bucketName;
    /**
     * 域节点
     */
    private String endPoint;
    /**
     * 默认文件路径前缀
     */
    private String prefix;
    /**
     * 阿里云域名
     */
    private String domain;

    private String accessKeyId;

    private String accessKeySecret;

    @Value("${storage.aliyun.oss.bucketName}")
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Value("${storage.aliyun.oss.endPoint}")
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    @Value("${storage.aliyun.oss.prefix}")
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Value("${storage.aliyun.oss.domain}")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Value("${storage.aliyun.oss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    @Value("${storage.aliyun.oss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDomain() {
        return domain;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }
}
