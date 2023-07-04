import auth from "./auth"
import tab from "./tab"

export default function installPlugins(app) {
  // 认证对象
  app.config.globalProperties.$auth = auth
  // 页签操作
  app.config.globalProperties.$tab = tab
}
