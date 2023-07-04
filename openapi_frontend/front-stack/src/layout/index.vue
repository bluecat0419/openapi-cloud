<template>
  <div class="app-wrapper">
    <nav-bar class="fix-header"></nav-bar>
    <router-view></router-view>
  </div>
</template>

<script setup>
import NavBar from "./Navbar/index.vue"
</script>

<style lang="scss" scoped>
.app-wrapper {
  position: relative;
  width: 100%;
  height: 100%;

  .fix-header {
    height: 50px;
  }
}
</style>
