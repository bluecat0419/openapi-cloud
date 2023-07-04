import Cookies from "js-cookie"

const TokenKey = "User-Token"
const expiresDay = 1

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token, { expires: expiresDay })
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
