package com.zty.pay.config;

import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;

import javax.annotation.Resource;

/**
 * 支付宝配置类
 */
public abstract class AlipayAbstractConfig {

    @Resource
    protected AliPayProperties aliPayProperties;

    protected void putConfig(){
        AliPayApiConfig aliPayApiConfig;
        try {
            aliPayApiConfig = AliPayApiConfigKit.getApiConfig(aliPayProperties.getAppId());
        } catch (Exception e) {
            aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(aliPayProperties.getAppId())
                    .setAliPayPublicKey(aliPayProperties.getPublicKey())
                    .setCharset("UTF-8")
                    .setPrivateKey(aliPayProperties.getPrivateKey())
                    .setServiceUrl(aliPayProperties.getServerUrl())
                    .setSignType("RSA2")
                    // 普通公钥方式
                    .build();
        }
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
    }

}
