import request from "@/utils/request"

// 生成订单后返回订单信息
export function generateOrder(data) {
  return request({
    url: `/interface-service/interfaceOrders/generateOrder`,
    method: "POST",
    data: data,
  })
}

// 创建订单
export function createOrder(data) {
  return request({
    url: `/interface-service/interfaceOrders/createOrder`,
    method: "POST",
    data: data,
  })
}

// 获取支付二维码
export function getPayQrCode(orderNo) {
  return request({
    url: `/interface-service/interfaceOrders/getPayQrCode/${orderNo}`,
    method: "GET",
  })
}
// 检查订单是否支付成功
export function checkPay(orderNo) {
  return request({
    url: `/interface-service/interfaceOrders/checkPay/${orderNo}`,
    method: "GET",
  })
}
// 用户的订单分页
export function myOrderPage(data) {
  return request({
    url: `/interface-service/interfaceOrders/myOrderPage`,
    method: "GET",
    params: data,
  })
}
// 用户的订单详情
export function myOrderDetail(orderId) {
  return request({
    url: `/interface-service/interfaceOrders/myOrderDetail/${orderId}`,
    method: "GET",
  })
}
