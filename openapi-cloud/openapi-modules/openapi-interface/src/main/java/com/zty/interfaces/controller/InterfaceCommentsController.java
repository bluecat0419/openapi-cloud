package com.zty.interfaces.controller;

import com.zty.interfaces.request.CommentRequest;
import com.zty.interfaces.service.InterfaceCommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 接口评论 Controller
 * @author ZhangTianYou
 */
@Api(tags = "接口评论 Controller")
@RestController
@RequestMapping("/interfaceComments")
public class InterfaceCommentsController {

    @Resource
    InterfaceCommentsService interfaceCommentsService;

    @PostMapping
    @ApiOperation("评论")
    public Long comment(@RequestBody @Validated CommentRequest request){
        return interfaceCommentsService.comment(request);
    }

}
