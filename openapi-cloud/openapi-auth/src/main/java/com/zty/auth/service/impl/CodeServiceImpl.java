package com.zty.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import com.zty.auth.entity.UserEntity;
import com.zty.auth.mapper.UserMapper;
import com.zty.auth.request.SendEmailCodeRequest;
import com.zty.auth.request.SendSmsCodeRequest;
import com.zty.auth.service.CodeService;
import com.zty.common.core.constant.Constant;
import com.zty.common.core.exception.BusinessException;
import com.zty.common.core.template.Template;
import com.zty.common.core.utils.AssertUtil;
import com.zty.common.core.utils.CodeUtils;
import com.zty.common.core.vo.UserDetails;
import com.zty.common.rabbitmq.constant.RabbitConstant;
import com.zty.common.rabbitmq.constant.SmsTemplate;
import com.zty.common.rabbitmq.dto.EmailMessageDTO;
import com.zty.common.rabbitmq.dto.SmsMessageDTO;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.mvcconfig.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhangTianYou
 */
@Slf4j
@Service
public class CodeServiceImpl implements CodeService {

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    UserMapper userMapper;

    @Override
    public void captcha(HttpServletResponse response, String uuid) {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(150, 48, 5);
        // 设置类型，纯大写字母
        specCaptcha.setCharType(Captcha.TYPE_ONLY_UPPER);
        // 输出图片流
        try {
            specCaptcha.out(response.getOutputStream());
        }catch (Exception e){
            throw new BusinessException("验证码生成失败,请重试");
        }

        //保存到 redis ,过期时间 3 分钟
        String key = RedisKeys.getCaptchaKey(uuid);
        redisTemplate.opsForValue().set(key,specCaptcha.text().trim(),180, TimeUnit.SECONDS);
    }

    @Override
    public Boolean sendSmsCode(SendSmsCodeRequest request) {
        //查询手机号是否注册
        UserEntity user = userMapper.getByPhone(request.getPhone());
        Long expire;
        if(request.getType()==0){
            AssertUtil.isTrue(user == null,"该手机号暂未注册");
            AssertUtil.isTrue(user.getStatus() == Constant.UserStatus.LOCKED.getValue(),"您的账号已被停用");
            //判断是否1分钟内是否发送过
            expire = redisTemplate.getExpire(RedisKeys.getSmsLoginCodeKey(request.getPhone()));
        }else{
            AssertUtil.isTrue(user != null,"该手机号已注册");
            //判断是否1分钟内是否发送过
            expire = redisTemplate.getExpire(RedisKeys.getSmsRegisterCodeKey(request.getPhone()));
        }

        AssertUtil.isTrue(expire > 60,"验证码已经发出,请勿频繁发送,请"+(expire - 60)+"秒后重试");

        //给短信服务发送消息
        SmsMessageDTO messageDTO=new SmsMessageDTO();
        messageDTO.setTemplate(request.getType()==0 ? SmsTemplate.LOGIN : SmsTemplate.REGISTER);
        messageDTO.setPhone(request.getPhone());
        messageDTO.setExpire(2);
        messageDTO.setRedisKey(request.getType()==0 ?
                RedisKeys.getSmsLoginCodeKey(request.getPhone()):RedisKeys.getSmsRegisterCodeKey(request.getPhone()));

        try {
            String message = JSON.toJSONString(messageDTO);

            MessageProperties messageProperties=new MessageProperties();
            messageProperties.setCorrelationId(request.getPhone());

            //向短信服务发送消息
            rabbitTemplate.send(RabbitConstant.authToSmsExchange,RabbitConstant.smsKey,new Message(message.getBytes(),messageProperties));
        } catch (Exception e) {
            log.error("发送短信验证码失败",e);
            return false;
        }

        return true;
    }

    @Override
    public Boolean sendLoginCodeEmail(SendEmailCodeRequest request) {
        //查询该邮箱是否已注册
        UserEntity user = userMapper.getByEmail(request.getEmail());
        Long expire;
        if(request.getType()==0){
            AssertUtil.isTrue(user == null,"该邮箱暂未注册");
            AssertUtil.isTrue(user.getStatus() == Constant.UserStatus.LOCKED.getValue(),"您的账号已被停用");
            //判断是否1分钟内是否发送过
            expire = redisTemplate.getExpire(RedisKeys.getEmailLoginCodeKey(request.getEmail()));
        }else{
            AssertUtil.isTrue(user != null,"该邮箱已注册");
            //判断是否1分钟内是否发送过
            expire = redisTemplate.getExpire(RedisKeys.getEmailRegisterCodeKey(request.getEmail()));
        }

        AssertUtil.isTrue(expire > 60,"邮件已经发出,请勿频繁发送,请"+(expire - 60)+"秒后重试");

        //生成验证码
        String code = CodeUtils.getCode();

        //给邮箱服务发送消息
        EmailMessageDTO messageDTO=new EmailMessageDTO();
        messageDTO.setSubject("openapi 开放平台验证码");
        messageDTO.setType(Constant.EmailType.CODE);
        messageDTO.setTargetEmail(request.getEmail());
        messageDTO.setCode(code);
        messageDTO.setExpire(2);
        messageDTO.setRedisKey(request.getType()==0 ? RedisKeys.getEmailLoginCodeKey(
                request.getEmail()):RedisKeys.getEmailRegisterCodeKey(request.getEmail())
        );
        messageDTO.setContent(request.getType()==0 ? Template.Email.loginCodeTemplate(code):Template.Email.registerCodeTemplate(code));

        try {
            String message = JSON.toJSONString(messageDTO);

            MessageProperties messageProperties=new MessageProperties();
            messageProperties.setCorrelationId(request.getEmail());

            //向邮箱服务发送消息
            rabbitTemplate.send(RabbitConstant.authToEmailExchange,RabbitConstant.emailKey,new Message(message.getBytes(),messageProperties));
        }catch (Exception e){
            log.error("发送邮箱验证码失败",e);
            return false;
        }
        return true;
    }

    @Override
    public Boolean sendAccessKeySmsCode(Integer type) {
        UserDetails user = UserContext.getUserInfo();
        AssertUtil.isTrue(StringUtils.isBlank(user.getMobile()),"您还未绑定手机号,请先绑定后重试");
        AssertUtil.isTrue(user.getStatus() == Constant.UserStatus.LOCKED.getValue(),
                HttpStatus.UNAUTHORIZED.value(), "您的账号已被停用");

        Long expire;
        if(type == 0){
            expire = redisTemplate.getExpire(RedisKeys.getSmsGenAccessKey(user.getEmail()));
        }else {
            expire = redisTemplate.getExpire(RedisKeys.getSmsGetSecretKey(user.getEmail()));
        }

        AssertUtil.isTrue(expire > 60,"验证码已经发出,请勿频繁发送,请"+(expire - 60)+"秒后重试");

        //给短信服务发送消息
        SmsMessageDTO messageDTO=new SmsMessageDTO();
        messageDTO.setTemplate(type==0 ? SmsTemplate.GEN_ACCESS_KEY:SmsTemplate.GET_SECRET_KEY);
        messageDTO.setPhone(user.getMobile());
        messageDTO.setExpire(2);
        messageDTO.setRedisKey(type==0 ?
                RedisKeys.getSmsGenAccessKey(user.getMobile()):RedisKeys.getSmsGetSecretKey(user.getMobile()));

        try {
            String message = JSON.toJSONString(messageDTO);

            MessageProperties messageProperties=new MessageProperties();
            messageProperties.setCorrelationId(user.getMobile());

            //向短信服务发送消息
            rabbitTemplate.send(RabbitConstant.authToSmsExchange,RabbitConstant.smsKey,new Message(message.getBytes(),messageProperties));
        } catch (Exception e) {
            log.error("发送短信验证码失败",e);
            return false;
        }

        return true;
    }

    @Override
    public Boolean sendAccessKeyEmailCode(Integer type) {
        UserDetails user = UserContext.getUserInfo();
        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()),"您还未绑定邮箱,请先绑定后重试");
        AssertUtil.isTrue(user.getStatus() == Constant.UserStatus.LOCKED.getValue(),
                HttpStatus.UNAUTHORIZED.value(), "您的账号已被停用");

        Long expire;
        if(type == 0){
            expire = redisTemplate.getExpire(RedisKeys.getEmailGenAccessKey(user.getEmail()));
        }else {
            expire = redisTemplate.getExpire(RedisKeys.getEmailGetSecretKey(user.getEmail()));
        }

        AssertUtil.isTrue(expire > 60,"验证码已经发出,请勿频繁发送,请"+(expire - 60)+"秒后重试");

        //生成验证码
        String code = CodeUtils.getCode();

        //给邮箱服务发送消息
        EmailMessageDTO messageDTO=new EmailMessageDTO();
        messageDTO.setSubject("openapi 开放平台验证码");
        messageDTO.setType(Constant.EmailType.CODE);
        messageDTO.setTargetEmail(user.getEmail());
        messageDTO.setCode(code);
        messageDTO.setExpire(2);
        messageDTO.setRedisKey(type==0 ? RedisKeys.getEmailGenAccessKey(user.getEmail()):RedisKeys.getEmailGetSecretKey(user.getEmail()));
        messageDTO.setContent(type==0 ? Template.Email.genGenAccessKeyCodeTemplate(code):Template.Email.getSecretKeyCodeTemplate(code));

        try {
            String message = JSON.toJSONString(messageDTO);

            MessageProperties messageProperties=new MessageProperties();
            messageProperties.setCorrelationId(user.getEmail());

            //向邮箱服务发送消息
            rabbitTemplate.send(RabbitConstant.authToEmailExchange,RabbitConstant.emailKey,new Message(message.getBytes(),messageProperties));
        }catch (Exception e){
            log.error("发送邮箱验证码失败",e);
            return false;
        }
        return true;
    }
}
