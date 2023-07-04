package com.zty.interfaces.controller;

import com.zty.common.core.constant.Constant;
import com.zty.common.core.groups.SaveGroup;
import com.zty.common.core.groups.UpdateGroup;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.entity.InterfacePackageEntity;
import com.zty.interfaces.request.SaveOrUpdateInterfacePackageRequest;
import com.zty.interfaces.service.InterfacePackageService;
import com.zty.interfaces.vo.InterfacePackageDetails;
import com.zty.mvcconfig.annotation.AuthCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 接口套餐 Controller
 * @author ZhangTianYou
 */
@Api(tags = "接口套餐 Controller")
@RestController
@RequestMapping("/interfacePackage")
public class InterfacePackageController {

    @Resource
    InterfacePackageService interfacePackageService;

    @AuthCheck
    @GetMapping("/page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示条数", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "interfaceInfoId", value = "接口id", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "packageName", value = "套餐名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "int", paramType = "query")
    })
    public PageData<InterfacePackageEntity> page(@ApiIgnore @RequestParam Map<String,Object> params){
        return interfacePackageService.page(params);
    }

    @AuthCheck
    @GetMapping("/{id}")
    @ApiOperation("根据id查询")
    public InterfacePackageDetails details(@PathVariable("id") Long id){
        return interfacePackageService.details(id);
    }

    @AuthCheck
    @PostMapping
    @ApiOperation("新增")
    public Long save(@RequestBody @Validated(SaveGroup.class)SaveOrUpdateInterfacePackageRequest request){
        return interfacePackageService.save(request);
    }

    @AuthCheck
    @PutMapping
    @ApiOperation("修改")
    public Boolean update(@RequestBody @Validated(UpdateGroup.class)SaveOrUpdateInterfacePackageRequest request){
        return interfacePackageService.update(request);
    }

    @AuthCheck
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Boolean delete(@PathVariable("id")Long id){
        return interfacePackageService.removeById(id);
    }

}
