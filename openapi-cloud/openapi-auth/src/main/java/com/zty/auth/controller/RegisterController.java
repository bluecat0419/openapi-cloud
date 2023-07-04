package com.zty.auth.controller;

import com.zty.auth.strategy.factory.UserTypeStrategyFactory;
import com.zty.common.core.vo.CommonResponse;
import com.zty.common.core.dto.RegisterDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 注册
 * @author ZhangTianYou
 */
@Api(tags = "注册 Controller")
@RestController
public class RegisterController {

    @PostMapping("/register")
    @ApiOperation("注册")
    public CommonResponse register(@RequestBody RegisterDTO register){
        return UserTypeStrategyFactory.getStrategy(register.getRegisterType()).register(register);
    }

}
