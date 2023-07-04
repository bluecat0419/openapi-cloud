import { login, logout, getInfo } from "@/api/login"
import { updateUserData } from "@/api/user/profile"
import { getToken, setToken, removeToken } from "@/utils/auth"
import defAva from "@/assets/images/profile.jpg"

const useUserStore = defineStore("user", {
  state: () => ({
    token: getToken(),
    userData: {
      avaUrl: defAva,
      email: null,
      gender: null,
      mobile: null,
      realName: null,
      username: null,
      accessKey: null,
    },
    roles: [],
    permissions: [],
  }),
  actions: {
    // 登录
    login(userData, loginType) {
      userData = { ...userData }
      const loginData = { ...userData, loginType }
      return new Promise((resolve, reject) => {
        login(loginData)
          .then((res) => {
            const { token } = res.data
            setToken(token)
            this.token = token
            resolve()
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    // 获取用户信息
    getInfo() {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res) => {
            const user = res.data
            const avaUrl =
              user.avaUrl === "" || user.avaUrl === null
                ? defAva
                : import.meta.env.VITE_APP_BASE_API + user.avaUrl

            this.userData.avaUrl = user.avaUrl === "" || user.avaUrl === null ? defAva : user.avaUrl
            this.userData.email = user.email
            this.userData.gender = user.gender
            this.userData.mobile = user.mobile
            this.userData.realName = user.realName
            this.roles = user.roles
            this.userData.username = user.username
            this.userData.accessKey = user.accessKey
            resolve(res)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    // 更新用户数据
    updateUserData(data) {
      const userData = { ...data }
      return new Promise((resolve, reject) => {
        updateUserData(userData).then((res) => {
          for (let attr of Object.keys(userData)) {
            if (attr in this.userData) {
              this.userData[attr] = userData[attr]
            }
          }
          setToken(res.data.token)
          this.token = res.data.token

          resolve(res)
        })
      })
    },
    // 退出系统
    logOut() {
      return new Promise((resolve, reject) => {
        logout()
          .then(() => {
            this.cleanData()
            resolve()
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    cleanData() {
      this.$reset()
      removeToken()
    },
    updateAccessKey(key) {
      return new Promise((resolve, reject) => {
        this.userData.accessKey = key
        resolve()
      })
    },
  },
  persist: true,
})

export default useUserStore
