import request from "@/utils/request"
//用户的订单分页
export function myOrderPage(data) {
  return request({
    url: "/interface-service/interfaceOrders/myOrderPage",
    method: "get",
    params: data,
  })
}

//用户的订单详情
export function myOrderDetail(orderId) {
  return request({
    url: `/interface-service/interfaceOrders/myOrderDetail/${orderId}`,
    method: "get",
  })
}

//订单退款
export function orderRefund(data) {
  return request({
    url: `/interface-service/interfaceOrders/orderRefund`,
    method: "POST",
    data,
  })
}
