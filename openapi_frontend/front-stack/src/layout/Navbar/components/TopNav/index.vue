<template>
  <el-menu :default-active="activeMenu" mode="horizontal" :ellipsis="false">
    <template v-for="(item, index) in topMenus">
      <el-menu-item
        @mouseenter="activeDropDown(item.path)"
        @mouseout="outMenu"
        :style="{ '--theme': theme }"
        :index="item.path"
        :key="index"
        v-if="index < visibleNumber"
      >
        {{ item.meta.title }}
        <top-drop-down></top-drop-down>
      </el-menu-item>
    </template>
  </el-menu>
</template>

<script setup>
import { constantRoutes } from "@/router"
import { isHttp } from "@/utils/validate"
import useAppStore from "@/store/modules/app"
import TopDropDown from "@/components/TopDropDown/index.vue"
import useSettingsStore from "@/store/modules/settings"
import usePermissionStore from "@/store/modules/permission"

// 顶部栏初始数
const visibleNumber = ref(null)
// 当前激活菜单的 index
const currentIndex = ref(null)

const appStore = useAppStore()
const settingsStore = useSettingsStore()
const permissionStore = usePermissionStore()
const route = useRoute()
const router = useRouter()

// 主题颜色
const theme = computed(() => settingsStore.theme)
// 顶部导航栏的路由信息
const routers = computed(() => permissionStore.topbarRouters)

// 顶部显示菜单
const topMenus = computed(() => {
  let topMenus = []
  routers.value.map((menu) => {
    if (menu.hidden !== true) {
      // 兼容顶部栏一级菜单内部跳转
      if (menu.path === "/") {
        topMenus.push(menu.children[0])
      } else {
        topMenus.push(menu)
      }
    }
  })
  return topMenus
})

// 设置子路由
const childrenMenus = computed(() => {
  let childrenMenus = []
  routers.value.map((router) => {
    for (let item in router.children) {
      if (router.children[item].parentPath === undefined) {
        if (router.path === "/") {
          router.children[item].path = "/" + router.children[item].path
        } else {
          if (!isHttp(router.children[item].path)) {
            router.children[item].path = router.path + router.children[item].path
          }
        }
        router.children[item].parentPath = router.path
      }
      childrenMenus.push(router.children[item])
    }
  })
  return constantRoutes.concat(childrenMenus)
})

// 激活下拉菜单
function activeDropDown(route) {
  appStore.setMenuStatus("hover")
  appStore.setDropdown(route)
}

function outMenu() {
  appStore.setMenuStatus("")
}

// 默认激活的菜单
const activeMenu = computed(() => {
  const path = route.path
  let activePath = path
  if (path !== undefined && path.lastIndexOf("/") > 0) {
    const tmpPath = path.substring(1, path.length)
    activePath = "/" + tmpPath.substring(0, tmpPath.indexOf("/"))
    if (!route.meta.link) {
    }
  } else if (!route.children) {
    activePath = path
  }
  activeRoutes(activePath)
  return activePath
})

function setVisibleNumber() {
  const width = document.body.getBoundingClientRect().width / 3
  visibleNumber.value = parseInt(width / 85)
}

function handleSelect(key, keyPath) {
  currentIndex.value = key
  const route = routers.value.find((item) => item.path === key)
  if (isHttp(key)) {
    // http(s):// 路径新窗口打开
    window.open(key, "_blank")
  } else if (!route || !route.children) {
    // 没有子路由路径内部打开
    const routeMenu = childrenMenus.value.find((item) => item.path === key)
    if (routeMenu && routeMenu.query) {
      let query = JSON.parse(routeMenu.query)
      router.push({ path: key, query: query })
    } else {
      router.push({ path: key })
    }
  } else {
    // 显示顶部菜单
    activeRoutes(key)
  }
}

function activeRoutes(key) {
  let routes = []
  console.log("childrenMenus: ", childrenMenus)
  if (childrenMenus.value && childrenMenus.value.length > 0) {
    childrenMenus.value.map((item) => {
      if (key == item.parentPath || (key == "index" && "" == item.path)) {
        routes.push(item)
      }
    })
  }
  if (routes.length > 0) {
    permissionStore.setTopbarRoutes(routes)
  }
  return routes
}

onMounted(() => {
  window.addEventListener("resize", setVisibleNumber)
})
onBeforeUnmount(() => {
  window.removeEventListener("resize", setVisibleNumber)
})

onMounted(() => {
  setVisibleNumber()
})
</script>

<style lang="scss">
.topmenu-container.el-menu--horizontal > .el-menu-item {
  float: left;
  height: 50px !important;
  line-height: 50px !important;
  color: #999093 !important;
  padding: 0 5px !important;
  margin: 0 10px !important;
}
</style>
