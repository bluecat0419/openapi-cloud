package com.zty.auth.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.zty.auth.common.constant.AuthConstant;
import com.zty.common.core.constant.Constant;
import com.zty.common.core.exception.BusinessException;
import com.zty.common.core.utils.AssertUtil;
import com.zty.common.core.utils.ConvertUtil;
import com.zty.auth.entity.UserEntity;
import com.zty.auth.service.TokenService;
import com.zty.common.core.vo.CommonResponse;
import com.zty.common.core.vo.UserDetails;
import com.zty.common.redis.keys.RedisKeys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
	/**
	 * 24小时后过期
	 */
	private final static int EXPIRE = 3600 * 24;

	@Resource
	RedisTemplate redisTemplate;

	@Override
	public CommonResponse createToken(UserEntity user) {
		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		UserDetails info = ConvertUtil.convert(user, UserDetails.class);

		String token = createToken(info,expireTime);
		redisTemplate.opsForValue().set(RedisKeys.getLoginUserKey(user.getId().toString()),token,EXPIRE,TimeUnit.SECONDS);

		Map<String, Object> map = new HashMap<>(1);
		map.put(Constant.TOKEN_HEADER, token);
		return new CommonResponse().ok(map);
	}

	@Override
	public void logout(Long userId) {
		String key = RedisKeys.getLoginUserKey(userId.toString());
		if(!redisTemplate.delete(key)){
			log.warn("redis delete key failed,key name:[{}]",key);
		}
	}

	/**
	 * 生成token
	 * @param info
	 * @param expireTime
	 * @return
	 */
	private String createToken(UserDetails info, Date expireTime){
		String token;
		try {
			token = Jwts.builder()
					// jwt payload --> KV   用户信息对象
					.claim(Constant.TOKEN_HEADER, JSON.toJSONString(info))
					// jwt id
					.setId(UUID.randomUUID().toString())
					// jwt 过期时间
					.setExpiration(expireTime)
					// jwt 签名 --> 加密
					.signWith(getPrivateKey(), SignatureAlgorithm.RS256)
					.compact();
		} catch (Exception e) {
			log.error("token生成失败"+e.getMessage(),e);
			throw new BusinessException("生成token失败,请联系管理员");
		}
		return token;
	}

	private PrivateKey getPrivateKey() throws Exception{
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
				new BASE64Decoder().decodeBuffer(AuthConstant.PRIVATE_KEY));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(priPKCS8);
	}
}
