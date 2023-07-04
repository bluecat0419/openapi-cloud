<template>
  <div class="register">
    <EntrancePanel>
      <template #title>注册</template>
      <template #content>
        <el-menu
          :default-active="mode"
          class="register-menu"
          mode="horizontal"
          :ellipsis="false"
          @select="selectRegisterMode"
        >
          <el-menu-item index="0">账号注册</el-menu-item>
          <el-menu-item index="1">手机注册</el-menu-item>
          <el-menu-item index="2">邮箱注册</el-menu-item>
        </el-menu>
        <keep-alive>
          <component
            :is="RegisterMode.get(mode)"
            v-model:registerForm="registerForm"
            @isValidate="isValidate"
          />
        </keep-alive>

        <div style="position: absolute; right: 25px; bottom: 25px">
          <router-link class="link-type" :to="'/login'">已有账号，去登录</router-link>
        </div>
      </template>
      <template #action>
        <el-form-item style="width: 100%">
          <el-button
            :loading="loading"
            size="large"
            type="primary"
            style="width: 100%"
            @click.prevent="handleRegister"
          >
            <span>
              注册
              <el-icon v-if="loading"><Loading /></el-icon>
            </span>
          </el-button>
        </el-form-item>
      </template>
    </EntrancePanel>
  </div>
</template>

<script setup>
import { ElMessage } from "element-plus"
import EntrancePanel from "../panel/index.vue"
import { RegisterMode } from "./mode"
import { register } from "@/api/register"
const router = useRouter()
const mode = ref("0")
const loading = ref(false)
const formValidate = ref(false)
const registerForm = reactive({
  captcha: "",
  code: "",
  email: "",
  password: "",
  phone: "",
  registerType: 0,
  username: "",
})

function selectRegisterMode(key) {
  mode.value = key
  for (let attr of Object.keys(registerForm)) {
    registerForm[attr] = ""
  }
  registerForm.registerType = Number(key)
  formValidate.value = false
}

function isValidate(validate) {
  if (formValidate.value !== validate) {
    formValidate.value = validate
  }
}

function handleRegister() {
  if (formValidate.value) {
    register(registerForm).then(() => {
      ElMessage({
        message: "注册成功",
        type: "success",
      })
      router.push("/login")
    })
  }
}

onUnmounted(() => {
  mode.value = "0"
})
</script>

<style lang="scss" scoped>
.register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../../../assets/images/login-background.jpg");
  background-size: cover;
}

.register-menu {
  margin-bottom: 10px;
}
</style>
