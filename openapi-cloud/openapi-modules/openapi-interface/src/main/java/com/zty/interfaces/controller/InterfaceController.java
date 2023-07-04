package com.zty.interfaces.controller;

import com.zty.common.core.constant.Constant;
import com.zty.common.core.groups.SaveGroup;
import com.zty.common.core.groups.UpdateGroup;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.entity.InterfaceInfoEntity;
import com.zty.interfaces.request.AddRegisterWelfareRequest;
import com.zty.interfaces.request.SaveOrUpdateInterfaceRequest;
import com.zty.interfaces.service.InterfaceService;
import com.zty.interfaces.vo.InterfaceInfoDetailsVO;
import com.zty.interfaces.vo.InterfaceInfoVO;
import com.zty.mvcconfig.annotation.AuthCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 接口信息
 * @author ZhangTianYou
 */
@Api(tags = "接口信息 Controller")
@RestController
@RequestMapping("/interfaces")
public class InterfaceController {

    @Resource
    InterfaceService interfaceService;

    @AuthCheck
    @GetMapping("/page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示条数", dataType = "int", paramType = "query")
    })
    public PageData<InterfaceInfoVO> page(@ApiIgnore  @RequestParam Map<String,Object> params){
        return interfaceService.page(params);
    }

    @AuthCheck
    @GetMapping("/{id}")
    @ApiOperation("根据id查询")
    public InterfaceInfoDetailsVO get(@PathVariable("id") Long id){
        return interfaceService.get(id);
    }

    @AuthCheck
    @GetMapping("/list")
    @ApiOperation("查询接口数据集合")
    public List<InterfaceInfoEntity> list(){
        return interfaceService.list();
    }

    @AuthCheck
    @PostMapping
    @ApiOperation("新增")
    public Long save(@RequestBody @Validated(SaveGroup.class) SaveOrUpdateInterfaceRequest request){
        return interfaceService.save(request);
    }

    @AuthCheck
    @PutMapping
    @ApiOperation("修改")
    public Boolean update(@RequestBody @Validated(UpdateGroup.class) SaveOrUpdateInterfaceRequest request){
        return interfaceService.update(request);
    }

    @AuthCheck
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Boolean delete(@PathVariable("id") Long id){
        return interfaceService.fakeDelete(id);
    }

    @AuthCheck
    @PostMapping("/addRegisterWelfare")
    @ApiOperation("添加注册后的奖励")
    public Boolean addRegisterWelfare(@RequestBody @Validated AddRegisterWelfareRequest request){
        return interfaceService.addRegisterWelfare(request);
    }

}
