import defaultSettings from "@/settings"
import { useDynamicTitle } from "@/utils/dynamicTitle"

const { navbarTheme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle } =
  defaultSettings

const localStorageSettings = JSON.parse(localStorage.getItem("layout-settings")) || ""

const useSettingsStore = defineStore("settings", {
  state: () => ({
    title: null,
    theme: localStorageSettings.theme || "#409EFF",
    navbarTheme: localStorageSettings.navbarTheme || navbarTheme,
    showSettings: showSettings,
    topNav: localStorageSettings.topNav === undefined ? topNav : localStorageSettings.topNav,
    tagsView:
      localStorageSettings.tagsView === undefined ? tagsView : localStorageSettings.tagsView,
    fixedHeader:
      localStorageSettings.fixedHeader === undefined
        ? fixedHeader
        : localStorageSettings.fixedHeader,
    sidebarLogo:
      localStorageSettings.sidebarLogo === undefined
        ? sidebarLogo
        : localStorageSettings.sidebarLogo,
    dynamicTitle:
      localStorageSettings.dynamicTitle === undefined
        ? dynamicTitle
        : localStorageSettings.dynamicTitle,
  }),
  actions: {
    // 修改布局设置
    changeSetting(data) {
      const { key, value } = data
      if (this.hasOwnProperty(key)) {
        this[key] = value
      }
    },
    // 设置网页标题
    setTitle(title) {
      this.title = title
      useDynamicTitle()
    },
  },
})

export default useSettingsStore
