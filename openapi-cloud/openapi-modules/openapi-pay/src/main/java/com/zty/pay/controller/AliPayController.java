package com.zty.pay.controller;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.zty.mvcconfig.annotation.IgnoreResponseAdvice;
import com.zty.pay.request.TradeQueryRequest;
import com.zty.pay.service.AliPayService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 支付宝支付
 * @author ZhangTianYou
 */
@Slf4j
@RestController
@RequestMapping("/alipay")
public class AliPayController{

    @Resource
    AliPayService aliPayService;

    @PostMapping("/notifyUrl")
    @ApiOperation("支付宝支付回调")
    @IgnoreResponseAdvice
    public String notifyUrl(HttpServletRequest request){
        return aliPayService.notifyUrl(request);
    }

    @PostMapping("/tradeQuery")
    @ApiOperation("支付宝交易查询")
    public AlipayTradeQueryResponse tradeQuery(@RequestBody TradeQueryRequest request){
        return aliPayService.tradeQuery(request);
    }

}
