import request from "@/utils/request"

// 首页：接口分页信息
export function getHomePage(data) {
  return request({
    url: "/interface-service/home/homepage",
    method: "get",
    params: data,
  })
}

// 首页：接口详细信息
export function getInterfaceDetail(id) {
  return request({
    url: `/interface-service/home/detail/${id}`,
    method: "get",
  })
}
// 首页：分页查询评论
export function getComments(data) {
  return request({
    url: `/interface-service/home/comments`,
    method: "get",
    params: data,
  })
}
// 首页：评论
export function interfaceComments(data) {
  return request({
    url: `/interface-service/interfaceComments`,
    method: "post",
    data,
  })
}
// 首页：接口文档信息
export function interfaceDoc(id) {
  return request({
    url: `/interface-service/home/interfaceDoc/${id}`,
    method: "get",
  })
}

// 生成 API 签名，用户在线调试接口
export function genAPISign(data) {
  return request({
    url: `/auth-service/user/genAPISign`,
    method: "post",
    data,
  })
}
