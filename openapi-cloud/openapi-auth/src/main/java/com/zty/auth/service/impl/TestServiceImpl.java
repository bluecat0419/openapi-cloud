package com.zty.auth.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl {

    @SentinelResource(value = "testService",fallback = "testServiceFallback",blockHandler = "handleException")
    public String testService(){
        return "testService";
    }

    public String handleException(BlockException e){
        //todo 记录日志
        return "testService触发限流啦";
    }

    public String testServiceFallback(Throwable e){
        return "出现异常啦,这是兜底数据";
    }

}
