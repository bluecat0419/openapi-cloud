package com.zty.sms.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zty.common.rabbitmq.constant.SmsTemplate;
import com.zty.common.rabbitmq.dto.SmsMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 国阳云短信客户端
 * @author ZhangTianYou
 */
@Slf4j
@Component
public class GYYSmsClient {

    private static Map<SmsTemplate,String> TEMPLATE_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        TEMPLATE_MAP.put(SmsTemplate.REGISTER,"05af942e52e0492a8044aa2f9c8a6c53");
        TEMPLATE_MAP.put(SmsTemplate.LOGIN,"c6cb3629a37948968c7c7fb48c90c16f");
        TEMPLATE_MAP.put(SmsTemplate.GEN_ACCESS_KEY,"6ee75df02d5e4353a0406ffc52b05208");
        TEMPLATE_MAP.put(SmsTemplate.GET_SECRET_KEY,"7876c29eeef84797ab8b77249a0c4562");
        signId = smsSignId;
        appCode = smsAppCode;
    }

    @Value("${gyy.smsSignId}")
    private String smsSignId;
    @Value("${gyy.smsAppCode}")
    private String smsAppCode;

    static String signId;
    static String appCode;

    /**
     * 发送短信
     * @param dto
     */
    public static void sendSms(SmsMessageDTO dto,String code) {
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","APPCODE " + appCode);
        HttpResponse response = HttpRequest.post("https://gyytz.market.alicloudapi.com/sms/smsSend?" + getParam(dto,code))
                .addHeaders(headers).execute();
        log.info("号码:[{}],国阳云短信发送结果：[{}]",dto.getPhone(),response.body());
    }

    /**
     * 获取请求参数
     * @param dto
     * @return
     */
    private static String getParam(SmsMessageDTO dto,String code){
        HashMap<String, String> param = new HashMap(5);
        param.put("mobile",dto.getPhone());
        param.put("param","**code**:"+code+",**minute**:"+dto.getExpire());
        param.put("smsSignId",signId);
        param.put("templateId",TEMPLATE_MAP.get(dto.getTemplate()));

        return paramToString(param);
    }

    /**
     * 将请求参数拼接成字符串
     * @param param
     * @return
     */
    private static String paramToString(Map<String,String> param){
        Iterator<String> var1 = param.keySet().iterator();
        StringBuilder res = new StringBuilder();
        while (var1.hasNext()) {
            String key = var1.next();
            String value = param.get(key);
            res.append(key +"="+ value);
            if(var1.hasNext()){
                res.append("&");
            }
        }
        return res.toString();
    }

}
