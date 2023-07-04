import { createRouter, createWebHashHistory } from "vue-router"
/* Layout */
import { Layout } from "@/components/routeComponent"

// 公共路由
export const constantRoutes = [
  {
    path: "/login",
    hidden: true,
    component: () => import("@/pages/entrance/login/index.vue"),
  },
  {
    path: "/register",
    hidden: true,
    component: () => import("@/pages/entrance/register/index.vue"),
  },
  {
    path: "",
    redirect: "/index",
    name: "Index",
    component: Layout,
    meta: { title: "首页", icon: "home" },
    children: [
      {
        path: "/index",
        component: () => import("@/pages/home/index.vue"),
        name: "Index",
        meta: { title: "首页", icon: "home" },
      },
      {
        path: "/document",
        component: () => import("@/pages/document.vue"),
        name: "Document",
        meta: { title: "文档", icon: "home" },
      },
    ],
  },
  {
    path: "/user",
    component: Layout,
    hidden: true,
    children: [
      {
        path: "profile",
        component: () => import("@/pages/user/profile/index.vue"),
        name: "Profile",
        meta: { title: "个人中心", icon: "user" },
      },
      {
        path: "order",
        component: () => import("@/pages/user/order/index.vue"),
        name: "Order",
        meta: { title: "我的订单", icon: "user" },
      },
    ],
  },
  {
    path: "/interface",
    component: Layout,
    hidden: true,
    children: [
      {
        path: "info",
        component: () => import("@/pages/interfaceInfo/index.vue"),
        name: "Info",
        meta: { title: "接口详情" },
      },
    ],
  },
  {
    path: "/order",
    component: Layout,
    hidden: true,
    children: [
      {
        path: "pay",
        component: () => import("@/pages/order/pay.vue"),
        name: "Pay",
        meta: { title: "订单", icon: "info" },
      },
    ],
  },
]

// 顶部导航路由
export const topnavRoutes = [
  {
    path: "/documentAndCommunity",
    name: "DocumentAndCommunity",
    component: Layout,
    meta: { title: "文档" },
    children: [
      {
        path: "/document",
        name: "Document",
        permissions: true,
        component: () => import("@/pages/document.vue"),
        meta: { title: "文档", icon: "fabulous" },
      }
    ],
  },
]

// 默认生成的路由（登录之后才会生成），因此是默认需要登录权限的，不然返回403页面
export const AddRoutes = [
  {
    path: "/test",
    component: Layout,
    children: [
      {
        path: "1",
        component: () => import("@/pages/test.vue"),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes: [...constantRoutes],
})

export default router
