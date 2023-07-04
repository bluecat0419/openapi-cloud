<template>
  <div class="mobile-register">
    <el-form
      ref="registerRef"
      :model="registerForm"
      :rules="registerRules"
      @validate="isValidate"
      class="register-form"
    >
      <el-form-item prop="phone">
        <el-input
          v-model="registerForm.phone"
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
          v-model="registerForm.code"
          size="large"
          auto-complete="off"
          placeholder="短信验证码"
          style="width: 60%"
        ></el-input>
        <div class="register-code">
          <el-button v-if="!timing" type="primary" @click="sendCode()">发送短信验证码</el-button>
          <el-button v-else type="primary" disabled>{{ timing }}</el-button>
        </div>
      </el-form-item>
      <el-form-item v-show="showPwdInput" prop="password">
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
    </el-form>
  </div>
</template>

<script setup>
import { ElMessage } from "element-plus"
import { codeTimer } from "./index"
import { isPhoneNumber } from "@/utils/validate"
import { reciprocalTime } from "@/utils/countdown"
import { registerPhoneCode } from "@/api/register"

const showPwdInput = ref(false)

const props = defineProps({
  registerForm: {
    type: Object,
    required: true,
  },
})
const emit = defineEmits(["isValidate"])

const registerForm = computed(() => props.registerForm)

const registerRules = {
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
  password: [{ required: true, trigger: "blur", message: "请设置密码" }],
}

// 当前验证码计时
const timing = ref(false)

function sendCode() {
  registerPhoneCode(registerForm.value.phone)
    .then((res) => {
      ElMessage({
        message: "发送成功",
        type: "success",
      })
      showPwdInput.value = true
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

onUnmounted(() => {
  showPwdInput.value = false
})
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
.register-code-img {
  height: 40px;
  padding-left: 12px;
}
</style>
