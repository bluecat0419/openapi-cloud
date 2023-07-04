import request from "@/utils/request"

/**
 * 获取分页的用户数据
 * @param {number} current 查询页码
 * @param {number} limit 每页的数量条数
 * @returns {object}
 */
export function pagingQueryUserData(current, limit, dataFrom) {
  return request({
    url: "/auth-service/user/page",
    method: "get",
    params: {
      current,
      limit,
      username: dataFrom.username,
      realName: dataFrom.realName,
      mobile: dataFrom.mobile,
      email: dataFrom.email,
      status: dataFrom.status,
    },
  })
}

/**
 * 通过id查询用户
 */
export function idQueryUserData(id) {
  return request({
    url: `/auth-service/user/${id}`,
    method: "get",
  })
}

/**
 * 添加用户
 * @param data
 */
export function saveUser(data) {
  return request({
    url: "/auth-service/user",
    method: "post",
    data: data,
  })
}

/**
 * 修改用户
 * @param data
 * @returns {*}
 */
export function updateUser(data) {
  return request({
    url: "/auth-service/user",
    method: "put",
    data: data,
  })
}

/**
 * 删除用户
 * @param id
 * @returns {*}
 */
export function deleteUser(id) {
  return request({
    url: `/auth-service/user/${id}`,
    method: "delete",
  })
}
