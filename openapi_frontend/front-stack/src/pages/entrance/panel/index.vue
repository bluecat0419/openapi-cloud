<template>
  <el-row class="entrance-wrapper" align="middle">
    <el-col :span="9">
      <left-aside />
    </el-col>
    <el-col :span="15" class="entrance-right">
      <h3>
        <slot name="title"></slot>
      </h3>
      <slot name="content"></slot>
      <slot name="action"></slot>
    </el-col>
  </el-row>
  <div class="el-login-footer">
    <span>Copyright © 2023 openapi All Rights Reserved.</span>
  </div>
</template>

<script setup>
import LeftAside from "./aside/index.vue"
</script>

<style lang="scss" scoped>
.entrance-wrapper {
  width: 640px;
  border-radius: 6px;
  overflow: hidden;
}

.el-col {
  height: 500px;
}

.entrance-right {
  width: 400px;
  padding: 25px 25px 5px 25px;
  background: #ffffff;
  .el-menu {
    margin-bottom: 10px;
  }
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>
