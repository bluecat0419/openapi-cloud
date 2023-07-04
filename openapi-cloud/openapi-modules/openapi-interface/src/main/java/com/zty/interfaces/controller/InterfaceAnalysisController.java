package com.zty.interfaces.controller;

import com.zty.interfaces.service.InterfaceAnalysisService;
import com.zty.interfaces.vo.InvokeCountAnalysisVO;
import com.zty.mvcconfig.annotation.AuthCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 接口分析 Controller
 * @author ZhangTianYou
 */
@Api(tags = "接口分析 Controller")
@RestController
public class InterfaceAnalysisController {

    @Resource
    InterfaceAnalysisService interfaceAnalysisService;

    @AuthCheck
    @GetMapping("/invokeCountAnalysis")
    @ApiOperation(value = "接口调用次数分析")
    public List<InvokeCountAnalysisVO> invokeCountAnalysis(@RequestParam(required = false,defaultValue = "top") String topOrTail){
        return interfaceAnalysisService.invokeCountAnalysis(topOrTail);
    }

}
