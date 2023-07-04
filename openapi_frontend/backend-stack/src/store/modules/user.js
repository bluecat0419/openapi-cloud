import { login, logout, getInfo } from "@/api/login"
import { getToken, setToken, removeToken } from "@/utils/auth"
import defAva from "@/assets/images/profile.jpg"
import { useRouter } from "vue-router"

const router = useRouter()

const useUserStore = defineStore("user", {
  id: "backend-user",
  state: () => ({
    token: getToken(),
    avaUrl: "",
    email: "",
    gender: "",
    mobile: "",
    realName: "",
    username: "",
    roles: [],
    permissions: [],
  }),
  actions: {
    // 登录
    login(userInfo) {
      userInfo = { ...userInfo }
      return new Promise((resolve, reject) => {
        login(userInfo)
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
              user.avaUrl == "" || user.avaUrl == null ? defAva : import.meta.env.VITE_APP_BASE_API + user.avaUrl

            this.avaUrl = user.avaUrl
            this.email = user.email
            this.gender = user.gender
            this.mobile = user.mobile
            this.realName = user.realName
            this.roles = user.roles
            this.username = user.username

            resolve(res)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    // 退出系统
    logOut() {
      return new Promise((resolve, reject) => {
        logout()
          .then(() => {
            this.token = ""

            this.avaUrl = ""
            this.email = ""
            this.gender = ""
            this.mobile = ""
            this.realName = ""
            this.roles = []
            this.permissions = []
            removeToken()
            resolve()
          })
          .catch((error) => {
            removeToken()
            reject(error)
          })
      })
    },
  },
})

export default useUserStore
