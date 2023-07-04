package com.zty.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.zty.common.core.constant.Constant;
import com.zty.common.core.utils.TokenParseUtil;
import com.zty.common.core.vo.CommonResponse;
import com.zty.common.core.vo.UserDetails;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.gateway.config.IgnoreUrlConfig;
import com.zty.gateway.constant.GatewayConstant;
import com.zty.gateway.utils.UrlFilterUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * 鉴权过滤器
 * @author ZhangTianYou
 */
@Slf4j
@Component
public class AuthGatewayFilter implements GatewayFilter, Ordered {

    @Resource
    RedisTemplate redisTemplate;
    @Resource
    IgnoreUrlConfig ignoreUrlConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        //如果是白名单则放行
        if(UrlFilterUtil.checkWhiteList(ignoreUrlConfig.getIgnoreUrls(),path)){
            return chain.filter(exchange);
        }

        HttpHeaders headers=request.getHeaders();
        String token = headers.getFirst(Constant.TOKEN_HEADER);
        if(StringUtils.isBlank(token)){
            return unauthorizedResponse(exchange,"令牌不能为空");
        }

        Jws<Claims> jws;

        try {
            jws = TokenParseUtil.parseToken(token);
            if(jws == null){
                return unauthorizedResponse(exchange,"令牌验证不正确");
            }
        } catch (Exception e) {
            log.error("解析token异常:[{}]",e.getMessage(),e);
            return unauthorizedResponse(exchange,"令牌解析错误");
        }

        Claims body = jws.getBody();
        if (body.getExpiration().before(Calendar.getInstance().getTime())){
            return unauthorizedResponse(exchange,"令牌已过期,请重新登陆");
        }

        UserDetails userInfo = TokenParseUtil.parseUserInfoFromClaims(body);

        Boolean isLogin = redisTemplate.hasKey(RedisKeys.getLoginUserKey(userInfo.getId().toString()));
        if(!isLogin){
            return unauthorizedResponse(exchange,"登录状态已过期,请重新登陆");
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange,String msg){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        CommonResponse result = new CommonResponse<>().error(HttpStatus.UNAUTHORIZED.value(),msg);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

}
