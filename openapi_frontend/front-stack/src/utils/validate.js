/**
 * 判断是否为外链
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 判断url是否是http或https
 * @param {string} url
 * @returns {Boolean}
 */
export function isHttp(url) {
  return url.indexOf("http://") !== -1 || url.indexOf("https://") !== -1
}

/**
 * 判断phone是否是11位国内的手机号格式
 * @param {number} phone
 * @returns {Boolean}
 */
export function isPhoneNumber(phone) {
  return /^1[3-9]\d{9}$/.test(phone)
}

/**
 * 判断email是否是邮箱格式
 * @param {string} email
 * @returns {Boolean}
 */
export function isEmail(email) {
  const reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
  return reg.test(email)
}
