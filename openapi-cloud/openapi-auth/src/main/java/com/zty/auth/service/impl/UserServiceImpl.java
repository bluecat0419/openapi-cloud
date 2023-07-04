package com.zty.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zty.auth.common.utils.PageUtil;
import com.zty.auth.entity.UserEntity;
import com.zty.auth.mapper.UserMapper;
import com.zty.auth.request.*;
import com.zty.auth.service.TokenService;
import com.zty.auth.service.UserService;
import com.zty.auth.vo.AccessKey;
import com.zty.auth.vo.UserInfo;
import com.zty.common.core.constant.Constant;
import com.zty.common.core.exception.BusinessException;
import com.zty.common.core.page.PageData;
import com.zty.common.core.utils.*;
import com.zty.common.core.vo.CommonResponse;
import com.zty.common.core.vo.UserDetails;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.mvcconfig.context.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Resource
    TokenService tokenService;
    @Resource
    RedisTemplate redisTemplate;

    /**
     * 构建查询条件
     * @param params
     * @return
     */
    private LambdaQueryWrapper<UserEntity> buildWrapper(Map<String,Object> params){
        String username = (String) params.get("username");
        String realName = (String) params.get("realName");
        String mobile = (String) params.get("mobile");
        String email = (String) params.get("email");
        Object status = params.get("status");

        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(username), UserEntity::getUsername, username)
                .like(StringUtils.isNotBlank(realName), UserEntity::getRealName, realName)
                .like(StringUtils.isNotBlank(mobile), UserEntity::getMobile, mobile)
                .like(StringUtils.isNotBlank(email), UserEntity::getEmail, email);
        if(status != null){
            wrapper.eq(UserEntity::getStatus, Integer.parseInt(status.toString()));
        }

        return wrapper;
    }

    @Override
    public UserInfo loginUser() {
        UserDetails user = UserContext.getUserInfo();
        UserInfo info = ConvertUtil.convert(this.getById(user.getId()), UserInfo.class);

        List<String> roles = new ArrayList<>();
        roles.add("user");
        if(user.getIsSuperAdmin()==1){
            roles.add("admin");
        }

        info.setRoles(roles);
        return info;
    }

    @Override
    public AccessKey genAccessKey(GenAccessKeyRequest request) {
        UserDetails user = UserContext.getUserInfo();

        UserEntity entity = baseMapper.selectById(user.getId());

        String cacheCode = "";
        switch (request.getCheckType()){
            case 0:
                AssertUtil.isTrue(StringUtils.isBlank(request.getPassword()),"密码不能为空");
                AssertUtil.isTrue(!MD5Util.valid(request.getPassword(),entity.getPassword()),"密码不正确");
                break;
            case 1:
                AssertUtil.isTrue(StringUtils.isBlank(entity.getMobile()),"手机号未绑定");
                cacheCode = (String) redisTemplate.opsForValue().get(RedisKeys.getSmsGenAccessKey(entity.getMobile()));
                break;
            case 2:
                AssertUtil.isTrue(StringUtils.isBlank(entity.getEmail()),"邮箱未绑定");
                cacheCode = (String) redisTemplate.opsForValue().get(RedisKeys.getEmailGenAccessKey(entity.getEmail()));
                break;
            default:
                throw new BusinessException("校验类型不正确");
        }
        //校验验证码
        if(request.getCheckType() != 0){
            AssertUtil.isTrue(StringUtils.isBlank(cacheCode),"验证码已失效");
            AssertUtil.isTrue(!StringUtils.equals(cacheCode,request.getCode()),"验证码不正确");
        }

        //生成 accessKey
        String accessKey = RandomUtil.randomStr(26);

        //生成 secretKey
        String secretKey = RandomUtil.randomStr(30);

        //保存数据
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(user.getId());
            userEntity.setAccessKey(accessKey);
            userEntity.setSecretKey(secretKey);
            baseMapper.updateById(userEntity);
        } catch (DuplicateKeyException e) {
            return genAccessKey(request);
        }

        return new AccessKey(accessKey,secretKey);
    }

    @Override
    public PageData<UserEntity> page(Map<String, Object> params) {
        ValidatedUtil.checkLimit(params);

        IPage<UserEntity> page = baseMapper.selectPage(PageUtil.getPage(params),buildWrapper(params));
        return new PageData<>(page.getRecords(),page.getTotal());
    }

    @Override
    public Boolean changePassword(ChangePasswordRequest request) {
        UserDetails loginUser = UserContext.getUserInfo();

        UserEntity user = baseMapper.getByUserName(loginUser.getUsername());
        AssertUtil.isTrue(request.getOldPassword().equals(request.getNewPassword()),"新密码不能与原密码相同");
        AssertUtil.isTrue(user.getStatus() == Constant.UserStatus.LOCKED.getValue(),"账号已被锁定，请联系管理员");
        AssertUtil.isTrue(!MD5Util.valid(request.getOldPassword(),user.getPassword()),"原密码错误");
        //密码加密
        user.setPassword(MD5Util.encStr(request.getNewPassword()));

        //退出登录
        tokenService.logout(loginUser.getId());
        return this.updateById(user);
    }

    @Override
    public Long save(SaveOrUpdateUserRequest request) {
        UserEntity entity = ConvertUtil.convert(request, UserEntity.class);
        entity.setEmail(StringUtils.isBlank(request.getEmail()) ? null:request.getEmail());
        entity.setMobile(StringUtils.isBlank(request.getMobile()) ? null:request.getMobile());
        //密码加密
        entity.setPassword(MD5Util.encStr(request.getPassword()));

        this.save(entity);

        return entity.getId();
    }

    @Override
    public Boolean update(SaveOrUpdateUserRequest request) {
        UserEntity entity = ConvertUtil.convert(request, UserEntity.class);
        entity.setEmail(StringUtils.isBlank(request.getEmail()) ? null:request.getEmail());
        entity.setMobile(StringUtils.isBlank(request.getMobile()) ? null:request.getMobile());
        //密码加密
        entity.setPassword(StringUtils.isBlank(request.getPassword()) ? null:MD5Util.encStr(request.getPassword()));

        return this.updateById(entity);
    }

    @Override
    public CommonResponse userUpdate(UserUpdateRequest request) {
        UserDetails loginUser = UserContext.getUserInfo();

        UserEntity entity = ConvertUtil.convert(request, UserEntity.class);
        entity.setId(loginUser.getId());
        entity.setEmail(StringUtils.isBlank(request.getEmail()) ? null:request.getEmail());
        entity.setMobile(StringUtils.isBlank(request.getMobile()) ? null:request.getMobile());

        try {
            this.updateById(entity);
        }catch (DuplicateKeyException e){
            throw new BusinessException("邮箱或手机号已存在,请修改后重试");
        }

        return tokenService.createToken(baseMapper.selectById(loginUser.getId()));
    }

    @Override
    public String getSecretKey(GetSecretKeyRequest request) {
        UserDetails user = UserContext.getUserInfo();

        UserEntity entity = baseMapper.selectById(user.getId());

        //校验
        String cacheCode = "";
        switch (request.getCheckType()){
            case 0:
                AssertUtil.isTrue(StringUtils.isBlank(request.getPassword()),"密码不能为空");
                AssertUtil.isTrue(!MD5Util.valid(request.getPassword(),entity.getPassword()),"密码不正确");
                break;
            case 1:
                AssertUtil.isTrue(StringUtils.isBlank(entity.getMobile()),"手机号未绑定");
                cacheCode = (String) redisTemplate.opsForValue().get(RedisKeys.getSmsGetSecretKey(entity.getMobile()));
                break;
            case 2:
                AssertUtil.isTrue(StringUtils.isBlank(entity.getEmail()),"邮箱未绑定");
                cacheCode = (String) redisTemplate.opsForValue().get(RedisKeys.getEmailGetSecretKey(entity.getEmail()));
                break;
            default:
                throw new BusinessException("校验类型不正确");
        }
        //校验验证码
        if(request.getCheckType() != 0){
            AssertUtil.isTrue(StringUtils.isBlank(cacheCode),"验证码已失效");
            AssertUtil.isTrue(!StringUtils.equals(cacheCode,request.getCode()),"验证码不正确");
        }

        return entity.getSecretKey();
    }

    @Override
    public String genAPISign(GenAPISignRequest request) {
        UserDetails user = UserContext.getUserInfo();

        UserEntity entity = baseMapper.selectById(user.getId());
        AssertUtil.isTrue(entity.getStatus() == Constant.UserStatus.LOCKED.getValue(),"账号已被停用,请联系管理员");
        AssertUtil.isTrue(StringUtils.isBlank(entity.getSecretKey()),"请先生成 AccessKey");

        return SignUtil.sign(entity.getSecretKey() + request.getNonce() + request.getTimestamp());
    }

}
