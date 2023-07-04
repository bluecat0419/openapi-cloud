import router from "./router"
import { ElMessage, ElMessageBox } from "element-plus"
import { getToken } from "@/utils/auth"
import { flattenArray } from "@/utils/openapi"
import { topnavRoutes } from "./router"
import { isRelogin } from "@/utils/request"
import useUserStore from "@/store/modules/user"

export let permission = false
const wihteList = ["/", "/interface/info", "/index", "/register", "/document"]
router.beforeEach((to, from, next) => {
  if (!getToken()) useUserStore().$reset()
  if (to.path == "/login") {
    if (getToken()) {
      next({ path: "/" })
    } else next()
  } else if (wihteList.includes(to.path)) {
    next()
  } else {
    if (getToken()) {
      next()
    } else {
      ElMessageBox.confirm("还未登录，请先登录", "系统提示", {
        confirmButtonText: "登录",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        isRelogin.show = false
        useUserStore().cleanData()
        next(`/login?redirect=${to.path}&query=${JSON.stringify(to.query)}`)
      })
    }
  }
})

function getPermissionRoutes(routes, suffix = "") {
  return flattenArray(
    routes.map((obj) => {
      if ("path" in obj) {
        if ("children" in obj) {
          return getPermissionRoutes(obj.children, obj.path)
        }
        return suffix + obj.path
      }
    }),
  )
}
