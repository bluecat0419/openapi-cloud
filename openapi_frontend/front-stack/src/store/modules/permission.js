import auth from "@/plugins/auth.js"
import { deepCopy } from "@/utils/openapi"
import router, { constantRoutes, topnavRoutes, AddRoutes } from "@/router"
import { Layout, ParentView, InnerLink } from "@/components/routeComponent"

// 匹配views里面所有的.vue文件
const modules = import.meta.glob("./../../pages/**/*.vue")

const usePermissionStore = defineStore("permission", {
  state: () => ({
    // 所有的路由
    routes: [],
    // 默认生成的总路由
    defaultAddRoutes: [],
    // 当前生成的路由
    addRoutes: [],
    // 顶部菜单栏的路由
    topbarRouters: [],
  }),
  actions: {
    setRoutes(routes) {
      this.addRoutes = routes
      this.routes = constantRoutes.concat(routes)
    },
    setDefaultAddRoutes(routes) {
      this.defaultAddRoutes = this.defaultAddRoutes.concat(routes)
    },
    setTopbarRoutes(routes) {
      this.topbarRouters = routes
    },
    generateRoutes(roles) {
      return new Promise((resolve) => {
        console.log("生成路由执行！")
        const all = constantRoutes.concat(topnavRoutes).concat(AddRoutes)
        const sRoute = deepCopy(AddRoutes)
        const rewriteRoutes = filterAsyncRouter(sRoute, false, true)
        const navRoutes = filterAsyncRouter(topnavRoutes)
        this.setRoutes(all)
        this.setDefaultAddRoutes(rewriteRoutes)
        this.setTopbarRoutes(navRoutes)
        resolve(rewriteRoutes)
      })
    },
  },
})

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter((route) => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component.toString() === "Layout") {
        route.component = Layout
      } else if (route.component.toString() === "ParentView") {
        route.component = ParentView
      } else if (route.component.toString() === "InnerLink") {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route["children"]
      delete route["redirect"]
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === "ParentView" && !lastRouter) {
        el.children.forEach((c) => {
          c.path = el.path + "/" + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + "/" + el.path
    }
    children = children.concat(el)
  })
  return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
  const res = []
  routes.forEach((route) => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

export const loadView = (page) => {
  let res
  for (const path in modules) {
    const dir = modules[path]
    if (dir.toString() === page.toString()) {
      res = () => dir()
    }
  }
  return res
}

export default usePermissionStore
