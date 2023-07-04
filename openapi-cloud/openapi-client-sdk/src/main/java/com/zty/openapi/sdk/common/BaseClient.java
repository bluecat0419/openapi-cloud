package com.zty.openapi.sdk.common;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.zty.openapi.sdk.common.exception.OpenApiSDKException;
import com.zty.openapi.sdk.common.utils.SignUtil;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ZhangTianYou
 */
public abstract class BaseClient {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    private Credential credential;

    public BaseClient(Credential credential){
        this.credential = credential;
    }

    protected HttpResponse doRequest(String url, BaseModel request,String method) throws OpenApiSDKException {
        HashMap<String, String> param = new HashMap();
        request.toMap(param);
        if(METHOD_GET.equals(method)){
            String a = url+"?"+paramToString(param);
            return HttpRequest.get(url+"?"+paramToString(param)).addHeaders(headerMap()).execute();
        }else if (METHOD_POST.equals(method)){
            return HttpRequest.post(url).addHeaders(headerMap()).body(JSONUtil.toJsonStr(param)).execute();
        }else {
            throw new OpenApiSDKException("Method only support (GET, POST)");
        }
    }

    private Map<String,String> headerMap(){
        Map<String,String> header=new HashMap<>(5);
        header.put("accessKey", credential.getAccessKey());
        header.put("nonce", String.valueOf(Math.abs((new SecureRandom()).nextInt())));
        header.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000L));
        header.put("sign", SignUtil.sign(credential.getSecretKey() + header.get("nonce") + header.get("timestamp")));
        return header;
    }

    private String paramToString(Map<String,String> param){
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
