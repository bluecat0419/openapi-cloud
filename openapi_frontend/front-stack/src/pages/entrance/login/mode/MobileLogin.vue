<template>
  <div class="mobile-login">
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <el-form-item prop="phone">
        <el-input
          v-model="loginForm.phone"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="手机号"
        >
          <template #prefix>
            <svg-icon icon-class="phone" class="el-input__icon input-icon" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          v-model="loginForm.code"
          size="large"
          auto-complete="off"
          placeholder="短信验证码"
          style="width: 60%"
          @keyup.enter="handleLogin"
        ></el-input>
        <div class="login-code">
          <el-button v-if="!timing" type="primary" @click="sendCode()">发送短信验证码</el-button>
          <el-button v-else type="primary" disabled>{{ timing }}</el-button>
        </div>
      </el-form-item>
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
import { ElMessage } from "element-plus"
import { getLoginType, codeTimer } from "./index"
import useUserStore from "@/store/modules/user"
import { isPhoneNumber } from "@/utils/validate"
import { reciprocalTime } from "@/utils/countdown"
import { sendPhoneCode } from "@/api/login"

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()
const { proxy, type: component } = getCurrentInstance()

const loginForm = ref({
  phone: "",
  code: "",
})

const loginRules = {
  phone: [
    {
      required: true,
      trigger: "blur",
      validator: (rule, value, callback) => {
        if (value === "") {
          callback(new Error("请输入您的手机号"))
        }
        if (isPhoneNumber(value)) {
          callback()
        } else {
          callback(new Error("手机格式不正确"))
        }
      },
    },
  ],
  code: [{ required: true, trigger: "blur", message: "请输入短信验证码" }],
}

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
// 当前验证码计时
const timing = ref(false)

function handleLogin() {
  proxy.$refs.loginRef.validate((valid) => {
    if (valid) {
      loading.value = true
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
          // 提示输入错误
        })
    }
  })
}

function sendCode() {
  sendPhoneCode(loginForm.value.phone)
    .then((res) => {
      ElMessage({
        message: "发送成功",
        type: "success",
      })
    })
    .then(() => {
      const { start } = reciprocalTime(codeTimer, (value) => {
        start()
        if (parseInt(value) !== 0) {
          timing.value = value
        } else {
          timing.value = false
        }
      })
    })
}
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
  width: auto;
  max-width: 40%;
  height: 40px;
  float: right;
  padding-left: 12px;
  button {
    height: inherit;
    vertical-align: middle;
  }
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
}
</style>
