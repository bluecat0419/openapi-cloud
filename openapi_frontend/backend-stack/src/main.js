import { createApp } from "vue"

import ElementPlus from "element-plus"
import locale from "element-plus/lib/locale/lang/zh-cn" //

import "@/assets/styles/index.scss" // global css

import App from "./App.vue"
import store from "./store"
import router from "./router"

// 注册指令
import plugins from "./plugins"

// svg 图标
import "virtual:svg-icons-register" // 插件注册脚本
import SvgIcon from "./components/SvgIcon/index.vue"
import elementIcon from "./components/SvgIcon/svgicon"

// 导入 MarkDown 插件
import mavonEditor from "mavon-editor"
import "mavon-editor/dist/css/index.css"

import "./permission" // permission control

const app = createApp(App)

app.use(router)
app.use(store)
app.use(plugins)
app.use(ElementPlus, { locale: locale, size: "default" })
app.use(elementIcon)
app.component("svg-icon", SvgIcon)
app.use(mavonEditor)

app.mount("#app")
