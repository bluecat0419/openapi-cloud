const useAppStore = defineStore("app", {
  state: () => ({
    navbar: {
      // 导航菜单的状态，比如"hover"
      menu: null,
      // 下拉菜单
      dropdown: {
        route: null,
        inElement: false,
      },
      // 是否隐藏navbar
      hide: false,
      withoutAnimation: false,
    },
    device: "desktop",
  }),
  getters: {
    openDropdown: (state) => state.navbar.menu === "hover" || state.navbar.dropdown.inElement,
  },
  actions: {
    setMenuStatus(status) {
      this.navbar.menu = status
    },
    inDropdown(status) {
      this.navbar.dropdown.inElement = status
    },
    setDropdown(route) {
      if (!route) {
        this.navbar.dropdown.route = null
      } else {
        this.navbar.dropdown.route = route
      }
    },
    toggleDevice(device) {
      this.device = device
    },
  },
})

export default useAppStore
