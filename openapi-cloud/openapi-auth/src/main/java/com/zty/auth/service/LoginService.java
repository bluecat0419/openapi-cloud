package com.zty.auth.service;

import com.zty.common.core.dto.BackLoginDTO;
import com.zty.common.core.vo.CommonResponse;
import com.zty.common.core.dto.LoginDTO;

/**
 * @author ZhangTianYou
 * @date 2022/10/25
 */
public interface LoginService {

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    CommonResponse login(LoginDTO loginDTO);

    /**
     * 后台登录
     * @param backLoginDTO
     * @return
     */
    CommonResponse backLogin(BackLoginDTO backLoginDTO);

    /**
     * 退出登录
     * @return
     */
    CommonResponse logout();

}
