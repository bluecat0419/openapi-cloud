package com.zty.auth.controller;

import com.openapi.sdk.common.Credential;
import com.openapi.sdk.common.exception.OpenApiSDKException;
import com.openapi.sdk.test.TestClient;
import com.openapi.sdk.test.model.TestRequest;
import com.openapi.sdk.test.model.TestResponse;
import com.zty.auth.request.SendEmailCodeRequest;
import com.zty.auth.request.SendSmsCodeRequest;
import com.zty.auth.service.CodeService;
import com.zty.common.core.vo.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证码
 * @author ZhangTianYou
 */
@Slf4j
@Api(tags = "验证码 Controller")
@RestController
@RequestMapping("/code")
public class CodeController {

    @Resource
    CodeService codeService;

    @GetMapping("/captcha")
    @ApiOperation("获取图形验证码")
    public void captcha(HttpServletResponse response,@RequestParam String uuid){
        codeService.captcha(response,uuid);
    }

    @ApiOperation("发送 登录/注册 短信验证码")
    @PostMapping("/sendSmsCode")
    public Boolean sendSmsCode(@RequestBody @Validated SendSmsCodeRequest request){
        return codeService.sendSmsCode(request);
    }

    @ApiOperation("发送 登录/注册 邮箱验证码")
    @PostMapping("/sendEmailCode")
    public Boolean sendLoginCodeEmail(@RequestBody @Validated SendEmailCodeRequest request){
        return codeService.sendLoginCodeEmail(request);
    }

    @GetMapping("/testInvoke")
    public CommonResponse test(){
        TestClient client = new TestClient(new Credential("9p61jr2ehoV1WT5Y517lg664Z9", "PO6474D08dZTHHDqiB3j4x7PaxUdC2"));
        TestRequest testRequest = new TestRequest();
        testRequest.setContent("张天佑");
        TestResponse response;
        try {
            response = client.getNameByGET(testRequest);
//            response = client.getNameByPOST(testRequest);
        } catch (OpenApiSDKException e) {
            log.error("调用接口失败:"+e.getMessage(),e);
            throw new RuntimeException(e);
        }
        return new CommonResponse().ok(response);
    }

}
