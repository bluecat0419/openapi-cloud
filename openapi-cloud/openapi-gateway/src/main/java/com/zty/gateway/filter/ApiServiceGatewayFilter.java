package com.zty.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.zty.common.core.utils.SignUtil;
import com.zty.common.core.vo.CommonResponse;
import com.zty.common.core.vo.UserDetails;
import com.zty.common.dubbo.dto.InterfaceInfoDubboDTO;
import com.zty.common.dubbo.provider.InterfaceProvider;
import com.zty.common.dubbo.provider.UserProvider;
import com.zty.common.redis.keys.RedisKeys;
import com.zty.gateway.constant.GatewayConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 接口服务网关过滤器
 *
 * @author ZhangTianYou
 */
@Slf4j
@Component
public class ApiServiceGatewayFilter implements GatewayFilter, Ordered {

    @DubboReference(timeout = 60000,retries=0)
    UserProvider userProvider;
    @DubboReference(timeout = 60000,retries=0)
    InterfaceProvider interfaceProvider;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    Executor executor;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + request.getURI().getPath());
        log.info("请求方法：" + request.getMethod().toString());
        log.info("请求来源地址：" + request.getRemoteAddress());

        //todo 校验是否是 ip 黑名单

        //获取请求头
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        if (StringUtils.isAnyBlank(accessKey,nonce,timestamp,sign)) {
            return handleError(exchange,"请求头参数不能为空");
        }

        InterfaceInfoDubboDTO interfaceInfo;
        UserDetails user;
        try {
            //校验 accessKey 是否存在
            user = userProvider.selectByAccessKey(accessKey);
            if(user == null){
                return handleError(exchange,"请确保 accessKey 存在并可用");
            }

            //防止请求重放
            if(!StringUtils.equals(SignUtil.sign(user.getSecretKey()+nonce+timestamp),sign)){
                return handleError(exchange,"签名认证失败");
            }
            Long currentTime = System.currentTimeMillis() / 1000;
            if ((currentTime - Long.parseLong(timestamp)) >= GatewayConstant.TIMESTAMP_EXPIRE_TIME){
                return handleError(exchange,"请求已过期");
            }
            String nonceKey=RedisKeys.getNonceKey(nonce);
            Boolean flag1 = redisTemplate.hasKey(nonceKey);
            if(flag1){
                return handleError(exchange,"请勿重复提交");
            }
            //将 nonce 保存到 redis 中并设置 1 分钟过期时间
            redisTemplate.opsForValue().set(nonceKey,"",60, TimeUnit.SECONDS);

            //校验接口是否存在并可用
            interfaceInfo = interfaceProvider.selectByUrlAndMethod(request.getURI().getPath(), request.getMethodValue());
            if(interfaceInfo == null){
                return handleError(exchange,"调用的接口不存在或已下线");
            }

            //校验用户是否拥有调用接口的权限,并且调用次数 >= 1
            Boolean flag2 = interfaceProvider.checkUserInterface(interfaceInfo.getId(), user.getId());
            if(!flag2){
                return handleError(exchange,"您暂无该接口调用权限");
            }
        }catch (Exception e){
            log.error("接口服务网关过滤器异常",e);
            return handleError(exchange,"未知错误,请稍后再试");
        }

        return handleResponse(exchange,chain,interfaceInfo.getId(),user.getId());
    }

    /**
     * 处理响应
     * @param exchange
     * @param chain
     * @param interfaceId
     * @param userId
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long interfaceId, Long userId){
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增 强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse){
                    //等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        CompletableFuture.runAsync(()->{
                            //接口总调用次数+1
                            redisTemplate.opsForValue().increment(RedisKeys.getApiAllInvokeCountKey());
                            redisTemplate.opsForZSet().incrementScore(RedisKeys.getApiInvokeCountKey(),interfaceId,1);
                        },executor);

                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        CompletableFuture.runAsync(()->{
                                            // 7. 调用成功，用户接口调用次数 - 1
                                            interfaceProvider.reduceUserInterfaceInvokeCount(interfaceId,userId);
                                        },executor);
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 设置 response 对象为装饰过的
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("网关处理响应异常"+e.getMessage(),e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -2;
    }

    private Mono<Void> handleError(ServerWebExchange exchange,String msg){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        CommonResponse result = new CommonResponse<>().error(HttpStatus.FORBIDDEN.value(),msg);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }
}
