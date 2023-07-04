import request from "@/utils/request"

// 查询所有系统文档信息标题集合
export function titleList() {
  return request({
    url: "/interface-service/document/common/titleList",
    method: "get",
  })
}

// 根据id查询
export function documentCommon(id) {
  return request({
    url: `/interface-service/document/common/${id}`,
    method: "get",
  })
}