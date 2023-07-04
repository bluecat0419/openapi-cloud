package com.zty.openapi.apiservice.test.controller;

import com.zty.mvcconfig.annotation.IgnoreResponseAdvice;
import com.zty.openapi.apiservice.common.utils.IPUtil;
import com.zty.openapi.apiservice.test.bo.TestBO;
import com.zty.openapi.apiservice.test.vo.TestVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试
 * @author ZhangTianYou
 */
@Api(tags = "测试 Controller")
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/testName")
    @IgnoreResponseAdvice
    public TestVO testByGET(String content, HttpServletRequest request){
        String test = request.getHeader("test");
        System.out.println("test = " + test);
        TestVO vo = new TestVO();
        vo.setIp(IPUtil.getIpFromRequest(request));
        vo.setName("GET您的名字是"+content);
        return vo;
    }

    @PostMapping("/testName")
    @IgnoreResponseAdvice
    public TestVO testByPOST(@RequestBody TestBO testBO, HttpServletRequest request){
        TestVO vo = new TestVO();
        vo.setIp(IPUtil.getIpFromRequest(request));
        vo.setName("POST您的名字是"+testBO.getContent());
        return vo;
    }

}
