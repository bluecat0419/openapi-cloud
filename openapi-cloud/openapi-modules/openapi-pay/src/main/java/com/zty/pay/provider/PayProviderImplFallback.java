package com.zty.pay.provider;

import com.alibaba.fastjson2.JSON;
import com.zty.common.dubbo.dto.OrderRefundDubboDTO;
import com.zty.common.dubbo.dto.QRCodePayDubboDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付服务降级处理类
 * @author ZhangTianYou
 */
@Slf4j
public class PayProviderImplFallback {

    public static String AliQRCodePayFallback(QRCodePayDubboDTO dto,Throwable e){
        log.error("创建订单失败:[{}]", JSON.toJSONString(dto),e);
        return "";
    }

    public static Boolean orderRefundFallback(OrderRefundDubboDTO dto,Throwable e){
        log.error("订单退款失败:[{}]", JSON.toJSONString(dto),e);
        return false;
    }

}
