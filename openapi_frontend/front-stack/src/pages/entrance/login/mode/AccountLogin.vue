<template>
  <div class="account-login">
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="账号"
        >
          <template #prefix>
            <svg-icon icon-class="user" class="el-input__icon input-icon" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          size="large"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter="handleLogin"
          type="password"
        >
          <template #prefix>
            <svg-icon icon-class="password" class="el-input__icon input-icon" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="captcha">
        <el-input
          v-model="loginForm.captcha"
          size="large"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter="handleLogin"
        ></el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img" />
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.isRememberMe" style="margin: 0px 0px 25px 0px">
        记住密码
      </el-checkbox>
      <el-form-item style="width: 100%">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          style="width: 100%"
          @click.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { getLoginType } from "./index"
import { getCaptchaImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from "@/utils/jsencrypt"
import useUserStore from "@/store/modules/user"

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
const { proxy, type: component } = getCurrentInstance()

const loginForm = ref({
  username: "",
  password: "",
  captcha: "",
  isRememberMe: false,
})

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  captcha: [{ required: true, trigger: "change", message: "请输入验证码" }],
}

const codeUrl = ref("")
const loading = ref(false)
const redirect = route.query.redirect ?? "/"
const query = JSON.parse(route.query.query ?? "{}")
const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
  if (cur !== "redirect") {
    acc[cur] = query[cur]
  }
  return acc
}, {})
// 登录方式
const loginType = computed(() => getLoginType(component.__name))

function handleLogin() {
  proxy.$refs.loginRef.validate((valid) => {
    if (valid) {
      loading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.isRememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 7 })
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 7 })
        Cookies.set("isRememberMe", loginForm.value.isRememberMe, { expires: 7 })
      } else {
        // 否则移除
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove("isRememberMe")
      }
      // 调用action的登录方法
      userStore
        .login(loginForm.value, loginType.value)
        .then(() => {
          userStore.getInfo().then(() => {
            router.push({ path: redirect, query: otherQueryParams })
          })
        })
        .catch(() => {
          loading.value = false
          // 重新获取验证码
          getCode()
        })
    }
  })
}

function getCode() {
  getCaptchaImg().then((res) => {
    let dataBlob = res
    const imgUrl = URL.createObjectURL(dataBlob)
    codeUrl.value = imgUrl
  })
}

function getCookie() {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const isRememberMe = Cookies.get("isRememberMe")
  loginForm.value = {
    username: username ? username : loginForm.value.username,
    password: password ? decrypt(password) : loginForm.value.password,
    isRememberMe: isRememberMe ? Boolean(isRememberMe) : false,
  }
}

getCode()
getCookie()
</script>

<style lang="scss" scoped>
.login-form {
  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
}
</style>
