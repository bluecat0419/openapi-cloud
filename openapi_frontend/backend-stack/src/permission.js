import router from "./router/index.js"
import { ElMessage } from "element-plus"
import { getToken } from "@/utils/auth"
import { isRelogin } from "@/utils/request"
import { isHttp } from "@/utils/validate"
import useUserStore from "@/store/modules/user"
import usePermissionStore from "@/store/modules/permission"

const whiteList = ["/login", "/register"]
let isRefreshPage = true

router.beforeEach((to, from, next) => {
  let permissions = useUserStore().roles
  if (getToken()) {
    if (!permissions.length) {
      isRelogin.show = true
      // 判断当前用户是否已拉取完user_info信息
      useUserStore()
        .getInfo()
        .then((res) => {
          permissions = res.data.roles
          const permission = permissions.includes("admin")
          if (permission) {
            isRelogin.show = false
            usePermissionStore()
              .generateRoutes()
              .then((accessRoutes) => {
                // 根据roles权限生成可访问的路由表
                accessRoutes.forEach((route) => {
                  if (!isHttp(route.path)) {
                    router.addRoute(route) // 动态添加可访问路由表
                  }
                })
                next({ ...to, replace: true })
              })
          } else {
            useUserStore()
              .logOut()
              .then(() => {
                ElMessage.warn("权限不足")
                next({ path: "/login", replace: true })
              })
          }
        })
        .catch((err) => {
          useUserStore()
            .logOut()
            .then(() => {
              ElMessage.error(err)
              next({ path: "/" })
            })
            .catch((e) => {
              console.log(e)
            })
        })
    } else {
      if (isRefreshPage) {
        usePermissionStore()
          .generateRoutes()
          .then((accessRoutes) => {
            // 根据roles权限生成可访问的路由表
            accessRoutes.forEach((route) => {
              if (!isHttp(route.path)) {
                router.addRoute(route) // 动态添加可访问路由表
              }
            })
            isRefreshPage = false
            next({ ...to, replace: true })
          })
      } else {
        next()
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next({ path: "/login", replace: true }) // 否则全部重定向到登录页, 且不允许用户回退
    }
  }
})

// 传入的 参数 routeObj 是一个对象
function addNestedRoutes(routeObj) {
  if (routeObj.path.charAt(0) === "/") {
    router.addRoute(routeObj)
  } else {
    let copyRoute = JSON.parse(JSON.stringify(routeObj))
    copyRoute.path = "/" + routeObj.path
    router.addRoute(copyRoute)
  }
  if (routeObj.children) {
    routeObj.children.forEach((child) => {
      addNestedRoutes(child)
    })
  }
}
