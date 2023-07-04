package com.zty.interfaces.controller;

import com.zty.common.core.constant.Constant;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.service.UserInterfaceInfoService;
import com.zty.interfaces.vo.MyInterfacePageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户接口信息 Controller
 * @author ZhangTianYou
 */
@Api(tags = "用户接口信息 Controller")
@RestController
@RequestMapping("/userInterfaceInfo")
public class UserInterfaceInfoController {

    @Resource
    UserInterfaceInfoService userInterfaceInfoService;

    @GetMapping("/myInterfacePage")
    @ApiOperation("我的接口分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示条数", dataType = "int", paramType = "query")
    })
    public PageData<MyInterfacePageVO> myInterfacePage(Map<String,Object> params){
        return userInterfaceInfoService.myInterfacePage(params);
    }

}
