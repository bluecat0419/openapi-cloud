<template>
  <el-dialog
    v-model="visible"
    :title="form.data.id != '' ? '修改' : '添加'"
    :close-on-press-escape="true"
    :before-close="close"
  >
    <el-form :model="form">
      <el-form-item label="用户名" label-width="120px">
        <el-input v-model="form.data.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" label-width="120px">
        <el-input type="password" v-model="form.data.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="真实名称" label-width="120px">
        <el-input v-model="form.data.realName" placeholder="请输入真实名称"></el-input>
      </el-form-item>
      <el-form-item label="性别" label-width="120px">
        <el-select v-model="form.data.gender" placeholder="请选择性别" clearable>
          <el-option label="男" :value="0" />
          <el-option label="女" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="邮箱" label-width="120px">
        <el-input v-model="form.data.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>
      <el-form-item label="手机号" label-width="120px">
        <el-input v-model="form.data.mobile" placeholder="请输入手机号"></el-input>
      </el-form-item>
      <el-form-item label="状态" label-width="120px">
        <el-radio-group v-model="form.data.status">
          <el-radio :label="1">正常</el-radio>
          <el-radio :label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button @click="close">取消</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { saveUser, updateUser, idQueryUserData } from "@/api/system/user"
import { initObjData } from "@/utils/openapi"
import { dataStructure } from "./user"
import { ElMessage } from "element-plus"

const visible = ref(false)
const form = reactive({
  data: {
    id: "",
    username: "",
    password: "",
    realName: "",
    gender: "",
    email: "",
    mobile: "",
    status: 1,
  },
})

function init() {
  visible.value = true
  if (form.data.id != "") {
    idQueryUserData(form.data.id).then((result) => {
      form.data.id = result.data.id
      form.data.username = result.data.username
      form.data.realName = result.data.realName
      form.data.gender = result.data.gender
      form.data.email = result.data.email
      form.data.mobile = result.data.mobile
      form.data.status = result.data.status
      form.data.password = ""
    })
  }
}

function close() {
  visible.value = false
  // 重置表单
  form.data = initObjData(dataStructure, "", { status: 1 })
}

function submit() {
  if (form.data.id == "") {
    saveUser(form.data).then((result) => {
      if (result.code != 0) {
        ElMessage.error(result.msg)
      }
      ElMessage.success("添加成功")
      close()
    })
  } else {
    updateUser(form.data).then((result) => {
      if (result.code != 0) {
        ElMessage.error(result.msg)
      }
      ElMessage.success("修改成功")
      close()
    })
  }
}

defineExpose({ init, form })
</script>
