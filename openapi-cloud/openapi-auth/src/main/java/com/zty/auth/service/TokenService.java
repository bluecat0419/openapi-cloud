package com.zty.auth.service;

import com.zty.auth.entity.UserEntity;
import com.zty.common.core.vo.CommonResponse;


/**
 * 用户Token
 */
public interface TokenService{

	/**
	 * 生成token
	 * @param user  用户信息
	 */
	CommonResponse createToken(UserEntity user);

	/**
	 * 退出
	 * @param userId  用户ID
	 */
	void logout(Long userId);


}
