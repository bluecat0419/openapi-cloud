package com.zty.gateway.filter.factory;

import com.zty.gateway.filter.ApiServiceGatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 接口服务网关过滤器工厂
 * @author ZhangTianYou
 */
@Component
public class ApiServiceGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Resource
    ApiServiceGatewayFilter apiServiceGatewayFilter;

    @Override
    public GatewayFilter apply(Object config) {
        return apiServiceGatewayFilter;
    }

}
