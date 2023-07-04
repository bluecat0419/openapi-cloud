import request from "@/utils/request"

/**
 * 修改用户信息
 * @param {Object} data { avaUrl, email , gender, mobile, realName }
 * @returns
 */
export function updateUserData(data) {
  return request({
    url: "/auth-service/user/userUpdate",
    method: "put",
    data,
  })
}

/**
 * 获得本地上传图片的url地址
 * @param {File} file 文件流
 * @returns
 */
export function getUploadUrl(file) {
  return request({
    url: "/file-service/upload",
    method: "post",
    headers: {
      "Content-Type": "multipart/form-data",
    },
    data: file,
  })
}
//修改密码
export function changePassword(data) {
  return request({
    url: "/auth-service/user/changePassword",
    method: "POST",
    data,
  })
}

//生成 accessKey
export function genAccessKey(data) {
  return request({
    url: "/auth-service/user/genAccessKey",
    method: "post",
    data,
  })
}
//查看 secretKey
export function getSecretKey(data) {
  return request({
    url: "/auth-service/user/getSecretKey",
    method: "post",
    data,
  })
}
//发送 生成accessKey/查看secretKey 邮箱验证码
export function sendAccessKeyEmailCode(type) {
  return request({
    url: `/auth-service/user/sendAccessKeyEmailCode/${type}`,
    method: "get",
  })
}
//发送 生成accessKey/查看secretKey 短信验证码
export function sendAccessKeySmsCode(type) {
  return request({
    url: `/auth-service/user/sendAccessKeySmsCode/${type}`,
    method: "get",
  })
}
//我的接口分页信息
export function myInterfacePage(data) {
  return request({
    url: "/interface-service/userInterfaceInfo/myInterfacePage",
    method: "get",
    query: data,
  })
}
