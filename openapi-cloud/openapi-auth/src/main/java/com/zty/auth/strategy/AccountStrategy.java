package com.zty.auth.strategy;

import com.zty.auth.common.utils.IpUtils;
import com.zty.auth.entity.LoginLogEntity;
import com.zty.auth.entity.UserEntity;
import com.zty.auth.mapper.LoginLogMapper;
import com.zty.auth.mapper.UserMapper;
import com.zty.auth.service.TokenService;
import com.zty.common.core.constant.Constant;
import com.zty.common.core.exception.BusinessException;
import com.zty.common.core.utils.AssertUtil;
import com.zty.common.core.utils.MD5Util;
import com.zty.common.core.vo.CommonResponse;
import com.zty.common.core.dto.LoginDTO;
import com.zty.common.core.dto.RegisterDTO;
import com.zty.common.dubbo.provider.InterfaceProvider;
import com.zty.common.redis.keys.RedisKeys;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


/**
 * 账号密码
 * @author ZhangTianYou
 * @date 2022/10/26
 */
@Component
public class AccountStrategy implements UserTypeStrategy {

    @Resource
    private UserMapper userMapper;
    @Resource
    private LoginLogMapper loginLogMapper;
    @Resource
    private TokenService tokenService;
    @DubboReference(timeout = 60000,retries=0)
    InterfaceProvider interfaceProvider;
    @Resource
    private HttpServletRequest request;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    Executor executor;


    @Override
    public CommonResponse login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername().trim();
        String password = loginDTO.getPassword().trim();

        //校验数据
        AssertUtil.isTrue(StringUtils.isBlank(username), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(password), "密码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(loginDTO.getCaptcha()),"验证码不能为空");

        //校验验证码
        String captcha = (String) redisTemplate.opsForValue().get(RedisKeys.getCaptchaKey(loginDTO.getUuid()));
        AssertUtil.isTrue(StringUtils.isBlank(captcha),"验证码已失效");
        AssertUtil.isTrue(!captcha.equalsIgnoreCase(loginDTO.getCaptcha().trim()),"验证码不正确");

        //根据用户名查询用户信息
        UserEntity user = userMapper.getByUserName(username);
        AssertUtil.isTrue(Objects.isNull(user), "用户名不存在");
        AssertUtil.isTrue(user.getIsSuperAdmin() == 1,"请输入正确的用户名");

        //判断该账号是否可用
        AssertUtil.isTrue(user.getStatus()==Constant.UserStatus.LOCKED.getValue(),"您的账号已被停用");

        //保存登录日志
        LoginLogEntity log = new LoginLogEntity();
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setLoginIp(IpUtils.getIpAddr(request));
        log.setType(Constant.Login.type_login.getValue());
        log.setUsername(user.getUsername());

        //校验密码
        if(!MD5Util.valid(password,user.getPassword())){
            //登录失败
            log.setStatus(Constant.Login.status_fail.getValue());
            loginLogMapper.insert(log);
            throw new BusinessException("密码不正确,请重试");
        }

        //登陆成功,保存数据库
        log.setStatus(Constant.Login.status_success.getValue());
        loginLogMapper.insert(log);

        //删除验证码缓存
        redisTemplate.delete(RedisKeys.getCaptchaKey(loginDTO.getUuid()));

        //生成 token
        return tokenService.createToken(user);
    }

    @Override
    public CommonResponse register(RegisterDTO registerDTO) {
        String username = registerDTO.getUsername().trim();
        String password = registerDTO.getPassword().trim();

        //校验数据
        AssertUtil.isTrue(StringUtils.isBlank(username) || username.trim().length() < 6, "请输入正确的用户名");
        AssertUtil.isTrue(StringUtils.isBlank(password) || password.trim().length() < 6, "请输入正确的密码");

        //校验验证码
        String captcha = (String) redisTemplate.opsForValue().get(RedisKeys.getCaptchaKey(registerDTO.getUuid()));
        AssertUtil.isTrue(StringUtils.isBlank(captcha),"验证码已失效");
        AssertUtil.isTrue(!captcha.equalsIgnoreCase(registerDTO.getCaptcha()),"验证码不正确");

        //根据用户名查询用户信息
        UserEntity user = userMapper.getByUserName(username);
        AssertUtil.isTrue(user != null, "用户名已存在,请修改后重试");

        //保存信息到数据库
        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setPassword(MD5Util.encStr(password));
        userMapper.insert(entity);

        CompletableFuture.runAsync(() -> {
            //删除验证码缓存
            redisTemplate.delete(RedisKeys.getCaptchaKey(registerDTO.getUuid()));
            //注册赠送活动
            interfaceProvider.registerWelfare(entity.getId());
        },executor);
        return new CommonResponse().ok();
    }
}
