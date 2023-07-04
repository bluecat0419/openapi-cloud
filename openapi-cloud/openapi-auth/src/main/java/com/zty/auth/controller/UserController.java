package com.zty.auth.controller;

import com.zty.auth.entity.UserEntity;
import com.zty.auth.request.*;
import com.zty.auth.service.CodeService;
import com.zty.auth.service.UserService;
import com.zty.auth.vo.AccessKey;
import com.zty.auth.vo.UserInfo;
import com.zty.common.core.constant.Constant;
import com.zty.common.core.groups.SaveGroup;
import com.zty.common.core.groups.UpdateGroup;
import com.zty.common.core.page.PageData;
import com.zty.common.core.vo.CommonResponse;
import com.zty.mvcconfig.annotation.AuthCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户
 * @author ZhangTianYou
 */
@Api(tags = "用户 Controller")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    CodeService codeService;

    @GetMapping("/page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示条数", dataType = "int", paramType = "query")
    })
    public PageData<UserEntity> page(@RequestParam Map<String,Object> params){
        return userService.page(params);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询")
    public UserEntity get(@PathVariable("id") Long id){
        return userService.getById(id);
    }

    @AuthCheck
    @PostMapping
    @ApiOperation("新增")
    public Long save(@RequestBody @Validated(SaveGroup.class) SaveOrUpdateUserRequest request){
        return userService.save(request);
    }

    @AuthCheck
    @PutMapping
    @ApiOperation("管理员修改")
    public Boolean update(@RequestBody @Validated(UpdateGroup.class) SaveOrUpdateUserRequest request){
        return userService.update(request);
    }

    @PutMapping("/userUpdate")
    @ApiOperation("用户修改")
    public CommonResponse userUpdate(@RequestBody UserUpdateRequest request){
        return userService.userUpdate(request);
    }

    @AuthCheck
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Boolean delete(@PathVariable("id") Long id){
        return userService.removeById(id);
    }

    @PostMapping("/changePassword")
    @ApiOperation("修改密码")
    public Boolean changePassword(@RequestBody @Validated ChangePasswordRequest request){
        return userService.changePassword(request);
    }

    @GetMapping("/loginUser")
    @ApiOperation("获取当前登录用户信息")
    public UserInfo loginUser(){
        return userService.loginUser();
    }

    @PostMapping("/getSecretKey")
    @ApiOperation("查看 secretKey")
    public String getSecretKey(@RequestBody @Validated GetSecretKeyRequest request){
        return userService.getSecretKey(request);
    }

    @PostMapping("/genAccessKey")
    @ApiOperation("生成 accessKey")
    public AccessKey genAccessKey(@RequestBody @Validated GenAccessKeyRequest request){
        return userService.genAccessKey(request);
    }


    @PostMapping("/genAPISign")
    @ApiOperation("生成 API 签名")
    public String genAPISign(@RequestBody @Validated GenAPISignRequest request){
        return userService.genAPISign(request);
    }

    @GetMapping("/sendAccessKeySmsCode/{type}")
    @ApiOperation("发送 生成accessKey/查看secretKey 短信验证码")
    public Boolean sendAccessKeySmsCode(@PathVariable("type") Integer type){
        return codeService.sendAccessKeySmsCode(type);
    }

    @GetMapping("/sendAccessKeyEmailCode/{type}")
    @ApiOperation("发送 生成accessKey/查看secretKey 邮箱验证码")
    public Boolean sendAccessKeyEmailCode(@PathVariable("type") Integer type){
        return codeService.sendAccessKeyEmailCode(type);
    }

}
