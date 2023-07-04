import axios from "axios"
import config from "@/config"
import { getToken } from "@/utils/auth"
import { tansParams } from "@/utils/openapi"
import errorCode from "@/utils/errorCode"
import router from "@/router"
import useUserStore from "@/store/modules/user.js"
import { ElNotification, ElMessageBox, ElMessage, ElLoading } from "element-plus"

// 是否显示重新登录
export let isRelogin = { show: false }

// 请求头
axios.defaults.headers["Content-Type"] = "application/json;charset=utf-8"
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: config.BaseApi,
  // 超时
  timeout: 10000,
})

// request拦截器
service.interceptors.request.use(
  (req) => {
    // 检查是否设置 token
    const isToken = (req.headers || {}).isToken === false
    // 是否防止数据重复提交
    const isRepeatSubmit = (req.headers || {}).repeatSubmit === false
    // 如果 token 存在，并且需要携带 token，那么在请求头中携带token
    if (getToken() && !isToken) {
      req.headers["token"] = getToken()
    }
    if (req.headers.url) {
      req.url = req.headers.url
      delete req.headers.url
    }
    return req
  },
  (error) => {
    console.log(error)
    Promise.reject(error)
  },
)

// 响应拦截器
service.interceptors.response.use(
  (res) => {
    // code 为 0 代表请求成功
    const code = res.data.code || 0
    // 获取错误信息
    const msg = (res.data.msg ?? errorCode[code]) || errorCode["default"]

    if (code === 401) {
      if (!isRelogin.show) {
        isRelogin.show = true
        ElMessageBox.confirm("登录状态已过期，您可以继续留在该页面，或者重新登录", "系统提示", {
          confirmButtonText: "重新登录",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            isRelogin.show = false
            useUserStore().cleanData()
            router.push("/login")
          })
          .catch(() => {
            isRelogin.show = false
          })
      }
      return Promise.reject("无效的会话，或者会话已过期，请重新登录。")
    } else if (code === 404 || code === 500) {
      ElMessage({ message: msg, type: "error" })
      return Promise.reject(new Error(msg))
    } else if (code !== 0) {
      ElNotification.error({ title: msg })
      return Promise.reject("error")
    } else {
      return Promise.resolve(res.data)
    }
  },
  (error) => {
    console.log("err: " + error)
    let { message } = error
    if (message == "Network Error") {
      message = "后端接口连接异常"
    } else if (message.includes("timeout")) {
      message = "系统接口请求超时"
    } else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常"
    }
    ElMessage({ message: message, type: "error", duration: 5 * 1000 })
    return Promise.reject(error)
  },
)

/**
 * 请求核心函数
 * @param {Object} options 请求配置
 * @param { Boolean } isMock 接口的mock开关
 */
function request(options, isMock = false) {
  if (config.env === "production") {
    service.defaults.baseURL = config.BaseApi
  } else if (config.mock) {
    service.defaults.baseURL = isMock ? config.MockApi : config.BaseApi
  }

  return service(options)
}

export default request
