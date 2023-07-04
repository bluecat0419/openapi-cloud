package com.zty.common.dubbo.provider;

import com.zty.common.dubbo.dto.OrderRefundDubboDTO;
import com.zty.common.dubbo.dto.QRCodePayDubboDTO;
import com.zty.common.dubbo.response.OrderRefundResponse;

/**
 * 支付服务
 * @author ZhangTianYou
 */
public interface PayProvider {

    /**
     * 扫码支付创建订单
     * @param dto   扫码支付入参对象
     * @return      二维码码串
     */
    String AliQRCodePay(QRCodePayDubboDTO dto);

    /**
     * 订单退款
     * @param dto   订单退款入参对象
     * @return      退款结果
     */
    OrderRefundResponse orderRefund(OrderRefundDubboDTO dto);

}
