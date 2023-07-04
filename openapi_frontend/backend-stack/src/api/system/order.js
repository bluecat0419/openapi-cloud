import request from "@/utils/request"

/**
 * 获取分页的订单数据
 * @param {number} current 查询页码
 * @param {number} limit 每页的数量条数
 * @returns {object}
 */
export function pagingQueryOrderData(current, limit, dataFrom) {
  return request({
    url: "/interface-service/interfaceOrders/page",
    method: "get",
    params: {
      current,
      limit,
      orderNo: dataFrom.orderNo,
      tradeNo: dataFrom.tradeNo,
      payType: dataFrom.payType,
      paymentStartDate: dataFrom.paymentStartDate,
      paymentEndDate: dataFrom.paymentEndDate,
      status: dataFrom.status,
    },
  })
}

/**
 * 通过id查询订单
 */
export function idQueryOrderData(id) {
  return request({
    url: `/interface-service/interfaceOrders/${id}`,
    method: "get",
  })
}

/**
 * 删除订单
 * @param id
 * @returns {*}
 */
export function deleteOrder(id) {
  return request({
    url: `/interface-service/interfaceOrders/${id}`,
    method: "delete",
  })
}
