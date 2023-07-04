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
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


/**
 * 邮箱
 * @author ZhangTianYou
 * @date 2022/11/9
 */
@Component
public class EmailStrategy implements UserTypeStrategy {

    @Resource
    UserMapper userMapper;
    @Resource
    LoginLogMapper sysLoginLogMapper;
    @Resource
    private TokenService tokenService;
    @Resource
    HttpServletRequest request;
    @DubboReference(timeout = 60000,retries=0)
    InterfaceProvider interfaceProvider;
    @Resource
    RedisTemplate<String, Object> redisTemplate;
    @Resource
    Executor executor;

    @Override
    public CommonResponse login(LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String code = loginDTO.getCode();

        //校验参数
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(code),"验证码不能为空");

        //根据邮箱查询用户
        UserEntity user = userMapper.getByEmail(email.trim());
        AssertUtil.isTrue(user == null,"该邮箱未注册,请先注册");
        AssertUtil.isTrue(user.getIsSuperAdmin() == 1,"请输入正确的邮箱");

        //判断该账号是否可用
        AssertUtil.isTrue(user.getStatus()==Constant.UserStatus.LOCKED.getValue(),"您的账号已被停用");

        //保存登录日志
        LoginLogEntity log = new LoginLogEntity();
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setLoginIp(IpUtils.getIpAddr(request));
        log.setType(Constant.Login.type_login.getValue());
        log.setUsername(email);

        //校验验证码
        String cacheCode = (String) redisTemplate.opsForValue().get(RedisKeys.getEmailLoginCodeKey(email));
        if(cacheCode == null){
            log.setStatus(Constant.Login.status_fail.getValue());
            sysLoginLogMapper.insert(log);
            throw new BusinessException("验证码不可用或已过期");
        }
        if(!cacheCode.equals(code.trim())){
            log.setStatus(Constant.Login.status_fail.getValue());
            sysLoginLogMapper.insert(log);
            throw new BusinessException("验证码错误");
        }

        //登陆成功
        log.setStatus(Constant.Login.status_success.getValue());
        sysLoginLogMapper.insert(log);

        //删除redis中的验证码
        redisTemplate.delete(RedisKeys.getEmailLoginCodeKey(email));

        //将token信息返回至前端
        return tokenService.createToken(user);
    }

    @Override
    public CommonResponse register(RegisterDTO registerDTO) {
        String email = registerDTO.getEmail();
        String code = registerDTO.getCode();
        String password = registerDTO.getPassword();

        //校验参数
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(code),"验证码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(password),"密码不能为空");

        //根据邮箱查询用户
        UserEntity user = userMapper.getByEmail(email.trim());
        AssertUtil.isTrue(user != null,"该邮箱已注册,请直接登录");

        //校验验证码
        String cacheCode = (String) redisTemplate.opsForValue().get(RedisKeys.getEmailRegisterCodeKey(email));
        AssertUtil.isTrue(cacheCode == null,"验证码不可用或已过期");
        AssertUtil.isTrue(!cacheCode.equals(code),"验证码错误");

        //保存信息到数据库
        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setUsername(email);
        entity.setPassword(MD5Util.encStr(password));
        userMapper.insert(entity);

        CompletableFuture.runAsync(() -> {
            //删除验证码缓存
            redisTemplate.delete(RedisKeys.getEmailRegisterCodeKey(email));
            //注册赠送活动
            interfaceProvider.registerWelfare(entity.getId());
        },executor);
        return new CommonResponse().ok();
    }
}
