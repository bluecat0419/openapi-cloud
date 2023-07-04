package com.zty.auth.controller;

import com.zty.auth.service.LoginService;
import com.zty.common.core.dto.BackLoginDTO;
import com.zty.common.core.vo.CommonResponse;
import com.zty.common.core.dto.LoginDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录
 * @author ZhangTianYou
 * @date 2022/10/25
 */
@Api(tags = "登录 Controller")
@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public CommonResponse login(@RequestBody LoginDTO loginDTO){
        return loginService.login(loginDTO);
    }

    @PostMapping("/backLogin")
    @ApiOperation("后台登录")
    public CommonResponse backLogin(@RequestBody BackLoginDTO backLoginDTO){
        return loginService.backLogin(backLoginDTO);
    }

    @GetMapping("/logOut")
    @ApiOperation("登出")
    public CommonResponse logout(){
        return loginService.logout();
    }

}
