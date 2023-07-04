<template>
  <router-view></router-view>
</template>

<script setup>
import useSettingsStore from "@/store/modules/settings"
import usePermissionStore from "@/store/modules/permission"
import { handleThemeStyle } from "@/utils/theme"

onMounted(() => {
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme)
  })
  usePermissionStore().generateRoutes()
})
</script>

<style lang="scss" scoped></style>
