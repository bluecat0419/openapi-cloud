import request from "@/utils/request"
import { uuid } from "@/utils/openapi"

// 生成随机的 uuid, 这里上下文一致
export const uuidRandom = uuid()

// 登录方法 -  账号密码登录
export function login(data) {
  data = Object.assign({}, data, { uuid: `${uuidRandom}` })
  return request({
    url: "/auth-service/backLogin",
    headers: {
      isToken: false,
    },
    method: "post",
    data: data,
  })
}

// 用户登出
export function logout() {
  return request({
    url: "/auth-service/logOut",
    method: "get",
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: "/auth-service/user/loginUser",
    method: "get",
  })
}

// 获取图形验证码
export function getCaptchaImg() {
  return request({
    url: "/auth-service/code/captcha",
    headers: {
      isToken: false,
    },
    method: "get",
    params: {
      uuid: `${uuidRandom}`,
    },
    responseType: "blob",
    timeout: 20000,
  })
}
