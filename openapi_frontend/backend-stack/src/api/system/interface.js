import request from "@/utils/request"

/**
 * 获取分页的接口数据
 * @param {number} current 查询页码
 * @param {number} limit 每页的数量条数
 * @returns {object}
 */
export function pagingQueryInterfaceData(current, limit, dataFrom) {
  const { name, url, method, responseType, status } = dataFrom
  return request({
    url: "/interface-service/interfaces/page",
    method: "get",
    params: {
      current,
      limit,
      name,
      url,
      method,
      responseType,
      status,
    },
  })
}

/**
 * 通过id查询接口信息
 */
export function idQueryInterfaceData(id) {
  return request({
    url: `/interface-service/interfaces/${id}`,
    method: "get",
  })
}

/**
 * 添加接口
 * @param data
 */
export function saveInterface(data) {
  return request({
    url: "/interface-service/interfaces",
    method: "post",
    data: data,
  })
}

/**
 * 修改接口
 * @param data
 * @returns {*}
 */
export function updateInterface(data) {
  return request({
    url: "/interface-service/interfaces",
    method: "put",
    data: data,
  })
}

/**
 * 删除接口
 * @param id
 * @returns {*}
 */
export function deleteInterface(id) {
  return request({
    url: `/interface-service/interfaces/${id}`,
    method: "delete",
  })
}

/**
 * 查询接口数据集合
 * @returns {*}
 */
export function queryList() {
  return request({
    url: `/interface-service/interfaces/list`,
    method: "get",
  })
}
