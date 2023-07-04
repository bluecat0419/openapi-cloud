package com.zty.pay.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ijpay.alipay.AliPayApi;
import com.zty.common.core.utils.AssertUtil;
import com.zty.common.dubbo.provider.InterfaceProvider;
import com.zty.pay.config.AlipayAbstractConfig;
import com.zty.pay.request.TradeQueryRequest;
import com.zty.pay.service.AliPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 支付宝支付服务实现类
 * @author ZhangTianYou
 */
@Slf4j
@Service
public class AliPayServiceImpl extends AlipayAbstractConfig implements AliPayService {
    @DubboReference(timeout = 60000,retries=0)
    InterfaceProvider interfaceProvider;

    @Override
    public String notifyUrl(HttpServletRequest request) {
        try {
            Map<String, String> params = AliPayApi.toMap(request);

            log.info("支付宝回调:[{}]", JSON.toJSONString(params));
            boolean verifyResult = AlipaySignature.rsaCheckV1(params,aliPayProperties.getPublicKey(), "UTF-8", "RSA2");

            if (verifyResult) {
                //付款成功,处理订单
                Boolean flag = interfaceProvider.orderSuccess(params);
                if(!flag){
                    return "fail";
                }
                log.info("notify_url 验证成功succcess");
                return "success";
            } else {
                log.error("notify_url 验证失败");
                return "fail";
            }
        }catch (Exception e) {
            log.error("支付宝支付回调失败", e);
        }
        return "fail";
    }

    @Override
    public AlipayTradeQueryResponse tradeQuery(TradeQueryRequest request) {
        putConfig();
        AssertUtil.isTrue(StringUtils.isAllBlank(request.getOrderNo(),request.getTradeNo()),"订单号和交易号不能同时为空");
        AlipayTradeQueryResponse response = null;
        try {
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            if (StringUtils.isNotEmpty(request.getOrderNo())) {
                model.setOutTradeNo(request.getOrderNo());
            }
            if (StringUtils.isNotEmpty(request.getTradeNo())) {
                model.setTradeNo(request.getTradeNo());
            }
            response = AliPayApi.tradeQueryToResponse(model);
        } catch (AlipayApiException e) {
            log.error("支付宝交易查询失败"+e.getMessage(), e);
        }
        return response;
    }
}
