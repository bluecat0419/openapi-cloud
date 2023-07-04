package com.zty.interfaces.controller;

import com.zty.common.core.constant.Constant;
import com.zty.common.core.exception.BusinessException;
import com.zty.common.core.page.PageData;
import com.zty.common.core.utils.QRCodeUtil;
import com.zty.interfaces.entity.InterfaceOrdersEntity;
import com.zty.interfaces.request.CreateOrderRequest;
import com.zty.interfaces.request.GenerateOrderRequest;
import com.zty.interfaces.request.OrderRefundRequest;
import com.zty.interfaces.service.InterfaceOrdersService;
import com.zty.interfaces.vo.GenerateOrderInfoVO;
import com.zty.interfaces.vo.InterfaceOrderDetailsVO;
import com.zty.interfaces.vo.MyOrderDetailVO;
import com.zty.interfaces.vo.MyOrderPageVO;
import com.zty.mvcconfig.annotation.AuthCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 接口订单 Controller
 * @author ZhangTianYou
 */
@Slf4j
@Api(tags = "接口订单 Controller")
@RestController
@RequestMapping("/interfaceOrders")
public class InterfaceOrdersController {

    @Resource
    InterfaceOrdersService interfaceOrdersService;

    @GetMapping("/page")
    @AuthCheck
    @ApiOperation("后台的订单分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示条数", dataType = "int", paramType = "query")
    })
    public PageData<InterfaceOrdersEntity> page(@RequestParam Map<String,Object> params){
        return interfaceOrdersService.page(params);
    }

    @GetMapping("/{id}")
    @AuthCheck
    @ApiOperation("根据id查询")
    public InterfaceOrderDetailsVO get(@PathVariable("id") Long id){
        return interfaceOrdersService.get(id);
    }

    @AuthCheck
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Boolean delete(@PathVariable("id") Long id){
        return interfaceOrdersService.fakeDelete(id);
    }

    @GetMapping("/myOrderPage")
    @ApiOperation("用户的订单分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示条数", dataType = "int", paramType = "query")
    })
    public PageData<MyOrderPageVO> myOrderPage(@RequestParam Map<String,Object> params){
        return interfaceOrdersService.myOrderPage(params);
    }

    @GetMapping("/myOrderDetail/{orderId}")
    @ApiOperation("用户的订单详情")
    public MyOrderDetailVO myOrderDetail(@PathVariable("orderId") Long orderId){
        return interfaceOrdersService.myOrderDetail(orderId);
    }

    @PostMapping("/generateOrder")
    @ApiOperation("生成订单")
    public GenerateOrderInfoVO generateOrder(@RequestBody @Validated GenerateOrderRequest request){
        return interfaceOrdersService.generateOrder(request);
    }

    @PostMapping("/createOrder")
    @ApiOperation("创建订单")
    public String createOrder(@RequestBody @Validated CreateOrderRequest request, HttpServletResponse response){
        return interfaceOrdersService.createOrder(request);
    }

    @GetMapping("/getPayQrCode/{orderNo}")
    @ApiOperation("获取支付二维码")
    public void getPayQrCode(@PathVariable("orderNo") String orderNo,HttpServletResponse response){
        InterfaceOrdersEntity order = interfaceOrdersService.selectByOrderNo(orderNo);
        try {
            QRCodeUtil.createCodeToOutputStream(order.getQrCode(), response.getOutputStream());
        } catch (Exception e) {
            log.error("生成二维码失败"+e.getMessage(), e);
            throw new BusinessException("订单创建失败,请重试或联系管理员");
        }
    }

    @GetMapping("/checkPay/{orderNo}")
    @ApiOperation("检查订单是否支付成功")
    public Boolean checkPay(@PathVariable("orderNo") String orderNo){
        return interfaceOrdersService.checkPay(orderNo);
    }

    @PostMapping("/orderRefund")
    @ApiOperation("订单退款")
    public Boolean orderRefund(@RequestBody @Validated OrderRefundRequest request){
        return interfaceOrdersService.orderRefund(request);
    }

}
