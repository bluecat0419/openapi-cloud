import request from "@/utils/request"

// 这里暂时不传 sort 字段

/**
 * 获取分页的系统文档信息
 * @param {number} current 查询页码
 * @param {number} limit 每页的数量条数
 * @returns
 */
export function pagingQuerySystemDocument(current, limit, title) {
  return request({
    url: "/interface-service/document/page",
    method: "get",
    params: {
      current,
      limit,
      title,
    },
  })
}

/**
 * 新增系统文档
 * @param {Object} data { title, content }
 * @returns
 */
export function createSystemDocument(data) {
  return request({
    url: "/interface-service/document",
    method: "post",
    data: data,
  })
}

/**
 * 修改系统文档
 * @param {Object} data { id, title, content }
 * @returns
 */
export function updateSystemDocument(data) {
  return request({
    url: "/interface-service/document",
    method: "put",
    data: data,
  })
}

/**
 * 通过删除查询系统文档
 * @param {*} id 系统文档id
 * @returns
 */
export function deleteSystemDocument(id) {
  return request({
    url: `/interface-service/document/{id}`,
    method: "delete",
  })
}

/**
 * 通过id查询系统文档信息
 * @param {*} id 系统文档id
 * @returns
 */
export function idQuerySystemDocument(id) {
  return request({
    url: `/interface-service/document/common/${id}`,
    method: "get",
  })
}

/**
 * 查询所有系统文档信息标题集合
 */
export function titleQueryAllSystemDocument() {
  return request({
    url: "/interface-service/document/common/titleList",
    method: "get",
  })
}
