import request from "@/utils/request"
import { uuidRandom } from "./login"

// 发送注册时短信验证码
export function registerPhoneCode(phone) {
  return request({
    url: "/auth-service/code/sendSmsCode",
    headers: {
      isToken: false,
    },
    method: "post",
    data: {
      phone,
      type: 1,
    },
  })
}

// 发送注册时邮箱验证码
export function registerEmailCode(email) {
  return request({
    url: "/auth-service/code/sendEmailCode",
    headers: {
      isToken: false,
    },
    method: "post",
    data: {
      email,
      type: 1,
    },
  })
}

/**
 * 用户注册
 * @param {object} data 注册信息（传入时不带多余字段）
 * @returns
 */
export function register(data) {
  const registerInfo = Object.assign({}, data, { uuid: uuidRandom })
  return request({
    url: "/auth-service/register",
    headers: {
      isToken: false,
    },
    method: "post",
    data: registerInfo,
  })
}
