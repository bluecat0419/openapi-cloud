package com.zty.interfaces.controller;

import com.zty.common.core.constant.Constant;
import com.zty.common.core.groups.SaveGroup;
import com.zty.common.core.groups.UpdateGroup;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.request.SaveOrUpdateDocumentRequest;
import com.zty.interfaces.service.DocumentService;
import com.zty.interfaces.vo.DocumentTitleVO;
import com.zty.interfaces.vo.DocumentVO;
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
 * 系统文档信息 Controller
 * @author ZhangTianYou
 */
@Api(tags = "系统文档信息 Controller")
@RestController
@RequestMapping("/document")
public class DocumentController {

    @Resource
    DocumentService documentService;

    @AuthCheck
    @GetMapping("/page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示条数", dataType = "int", paramType = "query")
    })
    public PageData<DocumentVO> page(@ApiIgnore @RequestParam Map<String,Object> params){
        return documentService.page(params);
    }

    @GetMapping("/common/{id}")
    @ApiOperation("根据id查询")
    public DocumentVO get(@PathVariable("id") Long id){
        return documentService.get(id);
    }

    @GetMapping("/common/titleList")
    @ApiOperation("查询所有系统文档信息标题集合")
    public List<DocumentTitleVO> titleList(){
        return documentService.titleList();
    }

    @AuthCheck
    @PostMapping
    @ApiOperation("新增")
    public Long save(@RequestBody @Validated(SaveGroup.class) SaveOrUpdateDocumentRequest request){
        return documentService.save(request);
    }

    @AuthCheck
    @PutMapping
    @ApiOperation("修改")
    public Boolean update(@RequestBody @Validated(UpdateGroup.class) SaveOrUpdateDocumentRequest request){
        return documentService.update(request);
    }

    @AuthCheck
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Boolean delete(@PathVariable("id") Long id){
        return documentService.fakeDelete(id);
    }

}
