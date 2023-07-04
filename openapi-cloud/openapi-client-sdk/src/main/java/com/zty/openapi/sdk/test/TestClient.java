package com.zty.openapi.sdk.test;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.zty.openapi.sdk.common.BaseClient;
import com.zty.openapi.sdk.common.Credential;
import com.zty.openapi.sdk.common.exception.OpenApiSDKException;
import com.zty.openapi.sdk.test.model.TestRequest;
import com.zty.openapi.sdk.test.model.TestResponse;

import static com.zty.openapi.sdk.common.constant.SDKConstant.*;

/**
 * 测试API客户端
 * @author ZhangTianYou
 */
public class TestClient extends BaseClient {

    private static final String API_URL = "/cloud/api-service/test/testName";

    public TestClient(Credential credential){
        super(credential);
    }

    public TestResponse getNameByGET(TestRequest request){
        TestResponse response;
        String result = "";
        try {
            HttpResponse res = this.doRequest(GATEWAY_HOST + API_URL, request,METHOD_GET);
            result = res.body();
            if(res.getStatus() != HttpStatus.HTTP_OK){
                throw new OpenApiSDKException("invoke api fail , code:["+res.getStatus()+"] ,message:["+result+"]");
            }
            response = JSONUtil.toBean(result,TestResponse.class);
        } catch (OpenApiSDKException e) {
            throw new OpenApiSDKException(e.getMessage());
        }
        return response;
    }

    public TestResponse getNameByPOST(TestRequest request){
        TestResponse response;
        String result = "";
        try {
            HttpResponse res = this.doRequest(GATEWAY_HOST + API_URL, request,METHOD_POST);
            result = res.body();
            if(res.getStatus() != HttpStatus.HTTP_OK){
                throw new OpenApiSDKException("Invoke API Fail , code:["+res.getStatus()+"] ,Message:["+result+"]");
            }
            response = JSONUtil.toBean(result,TestResponse.class);
        } catch (OpenApiSDKException e) {
            throw new OpenApiSDKException("Response Message: "+result+"Error Message: "+e.getMessage());
        }
        return response;
    }

}
