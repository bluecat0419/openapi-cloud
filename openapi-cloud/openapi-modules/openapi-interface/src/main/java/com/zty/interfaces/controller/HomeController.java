package com.zty.interfaces.controller;

import com.zty.common.core.constant.Constant;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.service.InterfaceCommentsService;
import com.zty.interfaces.service.InterfaceService;
import com.zty.interfaces.vo.InterfaceCommentVO;
import com.zty.interfaces.vo.InterfaceDocVO;
import com.zty.interfaces.vo.InterfaceHomeDetailVO;
import com.zty.interfaces.vo.InterfaceHomePageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 首页相关接口 Controller
 * 不需要鉴权的接口
 * @author ZhangTianYou
 */
@Api(tags = "首页相关接口 Controller")
@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    InterfaceService interfaceService;
    @Resource
    InterfaceCommentsService interfaceCommentsService;

    @GetMapping("/interfaceDoc/{id}")
    @ApiOperation("首页：接口文档信息")
    public InterfaceDocVO interfaceDoc(@PathVariable("id") Long id){
        return interfaceService.interfaceDoc(id);
    }

    @GetMapping("/homepage")
    @ApiOperation("首页：接口分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示条数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sortType", value = "排序类型  0:最新  1:评分  2:成交量", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "接口名称", dataType = "String", paramType = "query")
    })
    public PageData<InterfaceHomePageVO> homepage(@ApiIgnore @RequestParam Map<String,Object> params){
        return interfaceService.homepage(params);
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("首页：接口详细信息")
    public InterfaceHomeDetailVO detail(@PathVariable("id") Long id){
        return interfaceService.details(id);
    }

    @GetMapping("/comments")
    @ApiOperation("分页查询评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示条数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "interfaceInfoId", value = "接口id", dataType = "Long", paramType = "query")
    })
    public PageData<InterfaceCommentVO> page(@ApiIgnore @RequestParam Map<String,Object> params){
        return interfaceCommentsService.page(params);
    }

}
