package com.zty.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zty.auth.common.utils.IpUtils;
import com.zty.auth.entity.LoginLogEntity;
import com.zty.auth.entity.UserEntity;
import com.zty.auth.mapper.LoginLogMapper;
import com.zty.auth.mapper.UserMapper;
import com.zty.auth.service.LoginService;
import com.zty.auth.service.TokenService;
import com.zty.auth.strategy.factory.UserTypeStrategyFactory;
import com.zty.common.core.constant.Constant;
import com.zty.common.core.dto.BackLoginDTO;
import com.zty.common.core.exception.BusinessException;
import com.zty.common.core.utils.AssertUtil;
import com.zty.common.core.utils.MD5Util;
import com.zty.common.core.vo.CommonResponse;
import com.zty.common.core.dto.LoginDTO;
import com.zty.common.core.vo.UserDetails;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.mvcconfig.context.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @author ZhangTianYou
 * @date 2022/11/3
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private LoginLogMapper loginLogMapper;
    @Resource
    private HttpServletRequest request;
    @Resource
    TokenService tokenService;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    UserMapper userMapper;

    @Override
    public CommonResponse login(LoginDTO loginDTO) {
        return UserTypeStrategyFactory.getStrategy(loginDTO.getLoginType()).login(loginDTO);
    }

    @Override
    public CommonResponse backLogin(BackLoginDTO backLoginDTO) {
        String username = backLoginDTO.getUsername().trim();
        String password = backLoginDTO.getPassword().trim();

        //校验数据
        AssertUtil.isTrue(StringUtils.isBlank(username), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(password), "密码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(backLoginDTO.getCaptcha()),"验证码不能为空");

        //校验验证码
        String captcha = (String) redisTemplate.opsForValue().get(RedisKeys.getCaptchaKey(backLoginDTO.getUuid()));
        AssertUtil.isTrue(StringUtils.isBlank(captcha),"验证码已失效");
        AssertUtil.isTrue(!captcha.equalsIgnoreCase(backLoginDTO.getCaptcha().trim()),"验证码不正确");

        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getIsSuperAdmin, 1)
                .eq(UserEntity::getUsername, username);
        UserEntity user = userMapper.selectOne(wrapper);

        AssertUtil.isTrue(user == null, "用户名不存在");
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

        //生成 token
        return tokenService.createToken(user);
    }

    @Override
    public CommonResponse logout() {
        UserDetails user = UserContext.getUserInfo();

        tokenService.logout(user.getId());

        LoginLogEntity log = new LoginLogEntity();
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setLoginIp(IpUtils.getIpAddr(request));
        log.setType(Constant.Login.type_logout.getValue());
        log.setUsername(user.getUsername());
        log.setStatus(Constant.Login.status_success.getValue());
        loginLogMapper.insert(log);

        return new CommonResponse().ok();
    }

}
