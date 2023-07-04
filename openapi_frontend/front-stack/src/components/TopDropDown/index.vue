<template>
  <div
    @mouseover="changeDropdown(true)"
    @mouseout="changeDropdown(false)"
    :class="['navbar-dropdown', openDropdown ? 'active' : '']"
    :style="{
      backgroundColor:
        navbarTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground,
    }"
  >
  <!-- <div v-for="it in titleLists" :key="it.id" @click="goDocument(it.id)">{{ it.title }}</div> -->
  <div v-for="it in 4">lalalalla</div>
  </div>
</template>

<script setup>
import variables from "@/assets/styles/variables.module.scss"
import useSettingsStore from "@/store/modules/settings"
import useAppStore from "@/store/modules/app"
import {titleList} from '@/api/document/index.js'
const settingsStore = useSettingsStore()
const appStore = useAppStore()
//const router=userRouter()
const { navbarTheme } = storeToRefs(settingsStore)
const { openDropdown } = storeToRefs(appStore)

function changeDropdown(status) {
  appStore.inDropdown(status)
}
//下拉列表
let titleLists=ref([])
async function getTitleList(){
  let { data } = await titleList()
  titleLists.value=data
}
// function goDocument(id) {
//   router.push({path:'/document',query:{id}})
// }
</script>

<style lang="scss" scoped>
#app .navbar .navbar-dropdown{
  width: 100px;
  left: -30px;
}
.navbar-dropdown {
  z-index: 999;
  max-height: 0;
  overflow: hidden;
  transition: max-height 1s;
  &.active {
    max-height: calc(95vh - 50px);
  }

  .navbar-dropdown-wrapper {
    .el-col {
      &.introduce {
        border-right: 1px solid #e7eaec;
      }
    }
  }

  .dropdown-close {
    position: absolute;
    top: 10px;
    right: 10px;
  }
}
</style>
