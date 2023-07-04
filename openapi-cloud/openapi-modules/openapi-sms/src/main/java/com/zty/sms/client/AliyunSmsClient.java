package com.zty.sms.client;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ZhangTianYou
 * @date 2022/11/3
 */
@Slf4j
@Component
public class AliyunSmsClient {

    @Value("${sms.accessKey}")
    private String accessKey;
    @Value("${sms.accessKeySecret}")
    private String accessKeySecret;
    @Value("${sms.domainName}")
    private String domainName;
    @Value("${sms.signName}")
    private String smsSignName;
    @Value("${sms.templateCode}")
    private String smsTemplateCode;

    static Client client;
    static String signName;
    static String templateCode;

    @PostConstruct
    public void init() {
        signName=smsSignName;
        templateCode=smsTemplateCode;
        try {
            Config config=new Config().setAccessKeyId(accessKey).setAccessKeySecret(accessKeySecret);
            config.endpoint=domainName;
            client=new Client(config);
        } catch (Exception e) {
            log.error("阿里巴巴短信服务初始化失败,"+e.getMessage(),e);
        }
    }

    /**
     * 发送验证码
     * @param phone     需要发送的手机号
     * @param smsCode   验证码
     */
    public static void sendSms(String phone,String smsCode) throws Exception {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setPhoneNumbers(phone)
                .setTemplateParam(getTemplateParam(smsCode));
        RuntimeOptions runtime = new RuntimeOptions();
        // 复制代码运行请自行打印 API 的返回值
        client.sendSmsWithOptions(sendSmsRequest, runtime);
        log.info("验证码发送成功,手机号:{},验证码:{}",phone,smsCode);
    }

    private static String getTemplateParam(String code){
        return "{\"code\":\""+code+"\"}";
    }

}

