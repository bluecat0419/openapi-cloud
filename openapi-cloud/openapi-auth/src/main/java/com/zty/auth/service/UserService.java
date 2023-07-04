package com.zty.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zty.auth.entity.UserEntity;
import com.zty.auth.request.*;
import com.zty.auth.vo.AccessKey;
import com.zty.auth.vo.UserInfo;
import com.zty.common.core.page.PageData;
import com.zty.common.core.vo.CommonResponse;

import java.util.Map;

/**
 * 用户
 * @author ZhangTianYou
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 获取当前登录用户信息
     * @return
     */
    UserInfo loginUser();

    /**
     * 生成 accessKey
     * @param request
     * @return
     */
    AccessKey genAccessKey(GenAccessKeyRequest request);

    /**
     * 分页
     * @param params
     * @return
     */
    PageData<UserEntity> page(Map<String,Object> params);

    /**
     * 修改密码
     * @param request
     * @return
     */
    Boolean changePassword(ChangePasswordRequest request);

    /**
     * 添加
     * @param request
     * @return
     */
    Long save(SaveOrUpdateUserRequest request);

    /**
     * 管理员修改
     * @param request
     * @return
     */
    Boolean update(SaveOrUpdateUserRequest request);

    /**
     * 用户修改
     * @param request
     * @return
     */
    CommonResponse userUpdate(UserUpdateRequest request);

    /**
     * 查看 secretKey
     * @param request
     * @return
     */
    String getSecretKey(GetSecretKeyRequest request);

    /**
     * 生成 API 签名,用于在线调试
     * @param request
     * @return
     */
    String genAPISign(GenAPISignRequest request);
}
