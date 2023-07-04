export default {
  // 网页标题
  title: import.meta.env.VITE_APP_TITLE,
  // 导航栏主题
  navbarTheme: "theme-light",
  // 是否是系统布局配置
  showSettings: false,
  // 是否显示顶部导航
  topNav: false,
  // 是否显示tagsView
  tagsView: true,
  // 是否固定头部
  fixedHeader: false,
  // 是否显示logo
  sidebarLogo: true,
  // 是否显示动态标题
  dynamicTitle: false,

  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: "production",
}