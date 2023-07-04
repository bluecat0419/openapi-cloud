<template>
  <div class="login">
    <EntrancePanel>
      <template #title>登录</template>
      <template #content>
        <el-menu
          :default-active="mode"
          class="login-menu"
          mode="horizontal"
          :ellipsis="false"
          @select="selectLoginMode"
        >
          <el-menu-item index="0">账号登录</el-menu-item>
          <el-menu-item index="1">手机登录</el-menu-item>
          <el-menu-item index="2">邮箱登录</el-menu-item>
        </el-menu>
        <keep-alive>
          <component :is="LoginMode.get(mode)"></component>
        </keep-alive>
        <div style="position: absolute; right: 25px; bottom: 25px">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </template>
    </EntrancePanel>
  </div>
</template>

<script setup>
import EntrancePanel from "../panel/index.vue"
import { LoginMode } from "./mode"

const mode = ref("0")

function selectLoginMode(key) {
  mode.value = key
}

onUnmounted(() => {
  mode.value = "0"
})
</script>

<style lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../../../assets/images/login-background.jpg");
  background-size: cover;
}

.login-menu {
  margin-bottom: 10px;
}
</style>
