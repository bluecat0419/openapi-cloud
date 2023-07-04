package com.zty.pay.provider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.ijpay.alipay.AliPayApi;
import com.zty.common.core.utils.RandomUtil;
import com.zty.common.dubbo.dto.OrderRefundDubboDTO;
import com.zty.common.dubbo.dto.QRCodePayDubboDTO;
import com.zty.common.dubbo.provider.PayProvider;
import com.zty.common.dubbo.response.OrderRefundResponse;
import com.zty.pay.config.AlipayAbstractConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 支付服务实现类
 * @author ZhangTianYou
 */
@Slf4j
@DubboService
public class PayProviderImpl extends AlipayAbstractConfig implements PayProvider {

    @Override
    @SentinelResource(value = "AliQRCodePay",fallbackClass = PayProviderImplFallback.class,fallback = "AliQRCodePayFallback")
    public String AliQRCodePay(QRCodePayDubboDTO dto) {
        putConfig();
        // 支付宝回调地址
        String notifyUrl = aliPayProperties.getDomain() + "/cloud/pay-service/alipay/notifyUrl";

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(dto.getSubject());
        model.setTotalAmount(dto.getTotalAmount());
        model.setStoreId(dto.getStoreId());
        model.setTimeoutExpress("1440m");
        model.setBody(dto.getBody());
        model.setOutTradeNo(dto.getOrderNo());

        String qrCode = "";
        try {
            String resultStr = AliPayApi.tradePrecreatePayToResponse(model, notifyUrl).getBody();
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            qrCode = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
            return qrCode;
        } catch (Exception e) {
            log.error("支付宝创建交易失败"+e.getMessage(), e);
        }
        return qrCode;
    }

    @Override
    @SentinelResource(value = "orderRefund",fallbackClass = PayProviderImplFallback.class,fallback = "orderRefundFallback")
    public OrderRefundResponse orderRefund(OrderRefundDubboDTO dto) {
        putConfig();
        try {
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(dto.getOrderNo());
            if(StringUtils.isNotBlank(dto.getTradeNo())){
                model.setTradeNo(dto.getTradeNo());
            }
            model.setRefundAmount(dto.getRefundAmount());
            model.setRefundReason(dto.getRefundReason());

            AlipayTradeRefundResponse response = AliPayApi.tradeRefundToResponse(model);
            if(response.isSuccess()){
                if(StringUtils.equals(response.getFundChange(),"Y")){
                    log.info("支付宝退款成功:{}",dto.getOrderNo());
                    return new OrderRefundResponse(true,JSON.toJSONString(response));
                }
            }
            return new OrderRefundResponse(false,JSON.toJSONString(response));
        } catch (Exception e) {
            log.error("支付宝退款失败:"+dto.getOrderNo(),e);
        }
        return new OrderRefundResponse(false,null);
    }

}
