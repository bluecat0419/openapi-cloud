package com.zty.common.dubbo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单退款响应对象
 * @author ZhangTianYou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRefundResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 退款是否成功
     */
    private Boolean isSuccess;

    /**
     * 退款信息
     */
    private String refundInfo;

}
