package com.zty.auth.strategy;

import com.zty.common.core.vo.CommonResponse;
import com.zty.common.core.dto.LoginDTO;
import com.zty.common.core.dto.RegisterDTO;

/**
 * 用户类型策略
 * @author ZhangTianYou
 */
public interface UserTypeStrategy {

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    CommonResponse login(LoginDTO loginDTO);

    /**
     * 注册
     * @param registerDTO
     * @return
     */
    CommonResponse register(RegisterDTO registerDTO);

}
