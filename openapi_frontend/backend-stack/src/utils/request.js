import axios from "axios"
import { getToken } from "@/utils/auth"
import { tansParams } from "@/utils/openapi"
import errorCode from "@/utils/errorCode"
import useUserStore from "@/store/modules/user"

import { ElNotification, ElMessageBox, ElMessage, ElLoading } from "element-plus"

// 是否显示重新登录
export let isRelogin = { show: false }

// 请求头
axios.defaults.headers["Content-Type"] = "application/json;charset=utf-8"
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // 超时
  timeout: 10000,
})

// request拦截器
service.interceptors.request.use(
  (config) => {
    // 检查是否设置 token
    const isToken = (config.headers || {}).isToken === false
    // 是否防止数据重复提交
    const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
    // 如果 token 存在，并且需要携带 token，那么在请求头中携带token
    if (getToken() && !isToken) {
      config.headers["token"] = getToken()
    }

    // get 请求映射 params 参数
    if (config.method === "get" && config.params) {
      let url = config.url + "?" + tansParams(config.params)
      url = url.slice(0, -1)
      config.params = {}
      config.url = url
    }

    return config
  },
  (error) => {
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
        ElMessageBox.confirm("登录状态已过期，您可以继续留在该页面，或者重新登录", "系统提示", {
          confirmButtonText: "重新登录",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            isRelogin.show = true
            useUserStore()
              .logOut()
              .then(() => {
                location.href = import.meta.env.VITE_APP_BASE_API + "/login"
                location.reload()
              })
              .catch(() => {
                location.href = import.meta.env.VITE_APP_BASE_API + "/login"
                location.reload()
              })
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

export default service
