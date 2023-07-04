import request from "@/utils/request"

/**
 * 获取分页的套餐数据
 * @param {number} current 查询页码
 * @param {number} limit 每页的数量条数
 * @returns {object}
 */
export function pagingQueryPackageData(current, limit, dataFrom) {
  return request({
    url: "/interface-service/interfacePackage/page",
    method: "get",
    params: {
      current,
      limit,
      interfaceInfoId: dataFrom.interfaceInfoId,
      packageName: dataFrom.packageName,
      status: dataFrom.status,
    },
  })
}

/**
 * 通过id查询套餐信息
 */
export function idQueryPackageData(id) {
  return request({
    url: `/interface-service/interfacePackage/${id}`,
    method: "get",
  })
}

/**
 * 添加套餐
 * @param data
 */
export function savePackage(data) {
  return request({
    url: "/interface-service/interfacePackage",
    method: "post",
    data: data,
  })
}

/**
 * 修改套餐
 * @param data
 * @returns {*}
 */
export function updatePackage(data) {
  return request({
    url: "/interface-service/interfacePackage",
    method: "put",
    data: data,
  })
}

/**
 * 删除套餐
 * @param id
 * @returns {*}
 */
export function deletePackage(id) {
  return request({
    url: `/interface-service/interfacePackage/${id}`,
    method: "delete",
  })
}
