package com.zty.pay.service;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.zty.pay.request.TradeQueryRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付宝支付服务接口
 * @author ZhangTianYou
 */
public interface AliPayService {

    /**
     * 支付宝支付回调
     * @param request
     * @return
     */
    String notifyUrl(HttpServletRequest request);

    /**
     * 支付宝交易查询
     * @param request
     * @return
     */
    AlipayTradeQueryResponse tradeQuery(TradeQueryRequest request);

}
