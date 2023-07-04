import { createRouter, createWebHashHistory } from "vue-router"
/* Layout */
import { Layout } from "@/components/routeComponent.js"

// 公共路由
export const constantRoutes = [
  {
    path: "/login",
    hidden: true,
    component: () => import("@/pages/login.vue"),
  },
  {
    path: "",
    component: Layout,
    redirect: "/index",
    meta: { icon: "home" },
    children: [
      {
        path: "/index",
        component: () => import("@/pages/index.vue"),
        name: "Index",
        meta: { title: "首页", icon: "home" },
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import("@/pages/error/404.vue"),
    hidden: true,
  },
  {
    path: "/401",
    component: () => import("@/pages/error/401.vue"),
    hidden: true,
  },
]

// 首页路由
export const homeRoutes = [
  {
    path: "/test",
    name: "Test",
    hidden: true,
    component: () => import("@/pages/a.vue"),
  },
  {
    path: "/system",
    name: "System",
    component: Layout,
    hidden: false,
    meta: {
      title: "系统管理",
      icon: "system",
    },
    children: [
      {
        path: "interface",
        component: () => import("@/pages/system/interface/Interface.vue"),
        hidden: false,
        meta: { title: "接口管理", icon: "system" },
        name: "interface management",
      },
      {
        path: "package",
        component: () => import("@/pages/system/package/package.vue"),
        hidden: false,
        meta: { title: "套餐管理", icon: "system" },
        name: "package management",
      },
      {
        path: "order",
        component: () => import("@/pages/system/order/order.vue"),
        hidden: false,
        meta: { title: "订单管理", icon: "system" },
        name: "order management",
      },
      {
        path: "user",
        component: () => import("@/pages/system/user/user.vue"),
        hidden: false,
        meta: { title: "用户管理", icon: "user" },
        name: "user management",
      },
      {
        path: "log",
        hidden: false,
        meta: { title: "日志管理", icon: "tool" },
        name: "Log Management",
        children: [
          {
            path: "login",
            component: () => import("@/pages/system/log/log-login.vue"),
            hidden: false,
            meta: { title: "登录日志", icon: "monitor" },
            name: "Login of log",
          },
          {
            path: "operation",
            component: () => import("@/pages/system/log/log-operation.vue"),
            hidden: false,
            meta: { title: "操作日志", icon: "monitor" },
            name: "Operation Of Log",
          },
        ],
      },
      {
        path: "document",
        hidden: false,
        meta: { title: "文档管理", icon: "document" },
        name: "Document Management",
        component: () => import("@/pages/system/document/document.vue"),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes,
})

export default router
