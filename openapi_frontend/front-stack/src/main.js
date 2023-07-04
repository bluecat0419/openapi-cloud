import { createApp } from "vue"

import "@/assets/styles/index.scss" // global css
import "@/assets/styles/element-plus-demand.js"

import App from "./App.vue"
import store from "./store"
import router from "./router"

// svg 图标
import "virtual:svg-icons-register" // 插件注册脚本
import SvgIcon from "./components/SvgIcon/index.vue"
import elementIcon from "./components/SvgIcon/svgicon"

// 导入 MarkDown 插件
import mavonEditor from "mavon-editor"
import "mavon-editor/dist/css/index.css"

import "./permission" // permission control

const app = createApp(App)

app.use(router).use(store).use(elementIcon).use(mavonEditor)
app.component("svg-icon", SvgIcon)

app.mount("#app")
