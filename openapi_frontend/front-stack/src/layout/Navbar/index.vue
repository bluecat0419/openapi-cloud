<template>
  <div class="navbar">
    <div class="navbar-wrapper">
      <top-logo class="toplogo-container"></top-logo>
      <!-- <top-nav id="topmenu-container" class="topmenu-container" v-if="!settingsStore.topNav" /> -->

      <div class="right-menu">
        <template v-if="appStore.device !== 'mobile'">
          <header-search id="header-search" class="right-menu-item" />
          <screenfull id="screenfull" class="right-menu-item hover-effect" />
        </template>
        <div class="avatar-container">
          <el-dropdown
            @command="handleCommand"
            popper-class="account-tab-item"
            class="right-menu-item hover-effect"
            trigger="click"
          >
            <div class="avatar-wrapper">
              <img :src="userStore.userData.avaUrl" class="user-avatar" />
              <el-icon><caret-bottom /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <router-link to="/user/profile">
                  <el-dropdown-item>
                    <el-row justify="space-between" style="width: 120px">
                      <el-col :span="12">个人中心</el-col>
                      <el-col :span="6"><svg-icon icon-class="search"></svg-icon></el-col>
                    </el-row>
                  </el-dropdown-item>
                </router-link>
                <router-link to="/user/order">
                  <el-dropdown-item>
                    <el-row justify="space-between" style="width: 120px">
                      <el-col :span="12">我的订单</el-col>
                      <el-col :span="6"><svg-icon icon-class="search"></svg-icon></el-col>
                    </el-row>
                  </el-dropdown-item>
                </router-link>
                <el-dropdown-item v-if="isLogin" divided command="logout">
                  <span>退出登录</span>
                </el-dropdown-item>
                <router-link v-else to="/login">
                  <el-dropdown-item>
                    <span>去登录/注册</span>
                  </el-dropdown-item>
                </router-link>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    
  </div>
</template>

<script setup>
import { ElMessageBox } from "element-plus"

import TopLogo from "./components/TopLogo/index.vue"
//import TopNav from "./components/TopNav/index.vue"
import Screenfull from "@/components/Screenfull/index.vue"
import HeaderSearch from "@/components/HeaderSearch/index.vue"
import useAppStore from "@/store/modules/app"
import useUserStore from "@/store/modules/user"
import useSettingsStore from "@/store/modules/settings"
import { getToken } from "@/utils/auth"
import { useRouter } from "vue-router"
const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()
const isLogin = computed(() => getToken())
const router=useRouter()
function handleCommand(command) {
  switch (command) {
    case "setLayout":
      setLayout()
      break
    case "logout":
      logout()
      break
    default:
      break
  }
}

function logout() {
  ElMessageBox.confirm("确定注销并退出系统吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      userStore.logOut().then(() => {
        router.push('/')
      })
    })
    .catch(() => {})
}

const emits = defineEmits(["setLayout"])
function setLayout() {
  emits("setLayout")
}
</script>

<style lang="scss" scoped>
@import "@/assets/styles/mixin.scss";
.navbar {
  position: relative;
  background: #fff;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1) !important;

  .navbar-wrapper {
    @include clearfix;
    .toplogo-container {
      line-height: 50px;
      height: 100%;
      float: left;
      cursor: pointer;
      -webkit-tap-highlight-color: transparent;
    }

    .topmenu-container {
      position: absolute;
      left: 200px;
    }

    .errLog-container {
      display: inline-block;
      vertical-align: top;
    }

    .right-menu {
      float: right;
      height: 100%;
      line-height: 50px;
      display: flex;

      &:focus {
        outline: none;
      }

      .right-menu-item {
        display: inline-block;
        padding: 0 8px;
        height: 100%;
        font-size: 18px;
        color: #5a5e66;
        vertical-align: text-bottom;

        &.hover-effect {
          cursor: pointer;
          transition: background 0.3s;

          &:hover {
            background: rgba(0, 0, 0, 0.025);
          }
        }
      }

      .avatar-container {
        margin-right: 40px;

        .avatar-wrapper {
          margin-top: 5px;
          position: relative;

          .user-avatar {
            cursor: pointer;
            width: 40px;
            height: 40px;
            border-radius: 10px;
          }

          i {
            cursor: pointer;
            position: absolute;
            right: -20px;
            top: 25px;
            font-size: 12px;
          }
        }
        .account-tab-item {
          a {
            .el-space {
              .el-space__item:last-child() {
                margin-right: 0;
              }
            }
          }
        }
      }
    }
  }
}
</style>
