package com.zty.interfaces.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zty.common.core.page.PageData;
import com.zty.interfaces.entity.InterfaceOrdersEntity;
import com.zty.interfaces.request.CreateOrderRequest;
import com.zty.interfaces.request.GenerateOrderRequest;
import com.zty.interfaces.request.OrderRefundRequest;
import com.zty.interfaces.vo.GenerateOrderInfoVO;
import com.zty.interfaces.vo.InterfaceOrderDetailsVO;
import com.zty.interfaces.vo.MyOrderDetailVO;
import com.zty.interfaces.vo.MyOrderPageVO;

import java.util.Map;

/**
 * 接口订单
 * @Author ZhangTianYou
 */
public interface InterfaceOrdersService extends IService<InterfaceOrdersEntity>{

    /**
     * 后台的订单分页
     * @param params
     * @return
     */
    PageData<InterfaceOrdersEntity> page(Map<String,Object> params);

    /**
     * 根据id查询详细信息
     * @param id
     * @return
     */
    InterfaceOrderDetailsVO get(Long id);

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    boolean fakeDelete(Long id);

    /**
     * 用户的订单分页
     * @param params
     * @return
     */
    PageData<MyOrderPageVO> myOrderPage(Map<String,Object> params);

    /**
     * 订单详情
     * @param orderId       订单 id
     * @return
     */
    MyOrderDetailVO myOrderDetail(Long orderId);

    /**
     * 创建订单
     * @param request
     * @return      订单号
     */
    String createOrder(CreateOrderRequest request);

    /**
     * 订单退款
     * @param request
     * @return
     */
    Boolean orderRefund(OrderRefundRequest request);

    /**
     * 更新订单状态
     * @param orderNo       订单号
     * @param status        订单状态
     * @return
     */
    Boolean updateOrderStatus(String orderNo,Integer status);

    /**
     * 根据订单号查询订单
     * @param orderNo       订单号
     * @return
     */
    InterfaceOrdersEntity selectByOrderNo(String orderNo);

    /**
     * 生成订单
     * @param request
     * @return
     */
    GenerateOrderInfoVO generateOrder(GenerateOrderRequest request);

    /**
     * 检查订单是否支付成功
     * @param orderNo       订单号
     * @return
     */
    Boolean checkPay(String orderNo);
}
