<template>
  <div class="account-register">
    <el-form
      ref="registerRef"
      :model="registerForm"
      :rules="registerRules"
      @validate="isValidate"
      class="register-form"
    >
      <el-form-item prop="username">
        <el-input
          v-model="registerForm.username"
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
          v-model="registerForm.password"
          type="password"
          size="large"
          auto-complete="off"
          placeholder="密码"
        >
          <template #prefix>
            <svg-icon icon-class="password" class="el-input__icon input-icon" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="captcha">
        <el-input
          v-model="registerForm.captcha"
          size="large"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
        ></el-input>
        <div class="register-code">
          <img :src="codeUrl" @click="getCode" class="register-code-img" />
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { getCaptchaImg } from "@/api/login"

const props = defineProps({
  registerForm: {
    type: Object,
    required: true,
  },
})
const emit = defineEmits(["isValidate"])

const registerForm = computed(() => props.registerForm)

const registerRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  captcha: [{ required: true, trigger: "change", message: "请输入验证码" }],
}

const codeUrl = ref("")

function getCode() {
  getCaptchaImg().then((res) => {
    let dataBlob = res
    const imgUrl = URL.createObjectURL(dataBlob)
    codeUrl.value = imgUrl
  })
}

const validateMap = ref(new Map([]))
function isValidate(attr, validate) {
  if (!validateMap.value.has(attr) || validate !== validateMap.value.get(attr)) {
    validateMap.value.set(attr, validate)
  }
  emit(
    "isValidate",
    validateMap.value.size === 3 &&
      [...validateMap.value.values()].every((validate) => {
        return validate === true
      }),
  )
}

getCode()
</script>

<style lang="scss" scoped>
.register-form {
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
.register-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.register-code-img {
  height: 40px;
  padding-left: 12px;
}
</style>
