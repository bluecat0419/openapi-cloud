import { default as AccountLogin } from "./AccountLogin.vue"
import { default as MobileLogin } from "./MobileLogin.vue"
import { default as EmailLogin } from "./EmailLogin.vue"

export const LoginMode = new Map([
  ["0", AccountLogin],
  ["1", MobileLogin],
  ["2", EmailLogin],
])

export function getLoginType(componentName) {
  for (let [key, value] of LoginMode) {
    if (value.__name === componentName) {
      return key
    }
  }
  return null
}

// 发送验证码的计时
export const codeTimer = 60
