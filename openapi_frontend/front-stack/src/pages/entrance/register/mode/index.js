import { default as AccountRegister } from "./AccountRegister.vue"
import { default as MobileRegister } from "./MobileRegister.vue"
import { default as EmailRegister } from "./EmailRegister.vue"

export const RegisterMode = new Map([
  ["0", AccountRegister],
  ["1", MobileRegister],
  ["2", EmailRegister],
])

export function getRegisterType(componentName) {
  for (let [key, value] of RegisterMode) {
    if (value.__name === componentName) {
      return key
    }
  }
  return null
}

// 发送验证码的计时
export const codeTimer = 60
