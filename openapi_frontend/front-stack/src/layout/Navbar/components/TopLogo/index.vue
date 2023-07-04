<template>
  <div
    class="navbar-logo-container"
    :style="{
      backgroundColor:
        navbarTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground,
    }"
  >
    <router-link class="navbar-logo-link" to="/">
      <img v-if="logo" :src="logo" alt="" class="navbar-logo" />
      <h1
        class="navbar-title"
        :style="{
          color:
            navbarTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor,
        }"
      >
        {{ title }}
      </h1>
    </router-link>
    <router-link to="/document" class="navbar-logo-link" style="margin-left: 30px">
      <h1
        class="navbar-title"
        :style="{
          color:
            navbarTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor,
        }"
      >
        文档
      </h1>
    </router-link>
  </div>
</template>

<script setup>
import variables from "@/assets/styles/variables.module.scss"
import logo from "@/assets/logo/logo.jpg"
import useSettingsStore from "@/store/modules/settings"

const title = ref("API 开放平台")
const settingsStore = useSettingsStore()
const navbarTheme = computed(() => settingsStore.navbarTheme)
</script>

<style lang="scss" scoped>
.navbar-logo-container {
  position: relative;
  width: 260px;
  height: 50px;
  line-height: 50px;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;

  & .navbar-logo-link {
    height: 100%;
    width: 100%;

    & .navbar-logo {
      width: 32px;
      height: 32px;
      vertical-align: middle;
      margin-right: 12px;
    }

    & .navbar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }
}
</style>
