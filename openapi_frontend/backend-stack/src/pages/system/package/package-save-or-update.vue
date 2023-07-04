<template>
  <el-dialog
    v-model="visible"
    :title="form.data.id != '' ? '修改' : '添加'"
    :close-on-press-escape="true"
    :before-close="close"
  >
    <el-form :model="form">
      <el-form-item label="接口列表" label-width="120px">
        <el-select v-model="form.data.interfaceInfoId" placeholder="请选择接口" filterable clearable>
          <el-option v-for="item in interfaceList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="套餐名称" label-width="120px">
        <el-input v-model="form.data.name" placeholder="请输入套餐名称"></el-input>
      </el-form-item>
      <el-form-item label="接口调用次数" label-width="120px">
        <el-input type="number" v-model="form.data.invokeCount" placeholder="请输入接口调用次数"></el-input>
      </el-form-item>
      <el-form-item label="价格" label-width="120px">
        <el-input type="number" v-model="form.data.price" placeholder="请输入价格"></el-input>
      </el-form-item>
      <el-form-item label="折扣" label-width="120px">
        <el-input type="number" v-model="form.data.discount" placeholder="请输入价格"></el-input>
      </el-form-item>
      <el-form-item label="过期时间" label-width="120px">
        <el-date-picker v-model="form.data.expireDate" type="datetime" placeholder="请选择过期时间" />
      </el-form-item>
      <el-form-item label="状态" label-width="120px">
        <el-radio-group v-model="form.data.status">
          <el-radio :label="0">可用</el-radio>
          <el-radio :label="1">不可用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否主页显示" label-width="120px">
        <el-radio-group v-model="form.data.showPrice">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
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
import { reactive, ref } from "vue"
import { queryList } from "@/api/system/interface"
import { initObjData } from "@/utils/openapi"
import { dataStructure } from "./package"
import { savePackage, updatePackage, idQueryPackageData } from "@/api/system/package"
import { ElMessage } from "element-plus"

const visible = ref(false)
const form = reactive({
  data: {
    id: "",
    interfaceInfoId: "",
    name: "",
    invokeCount: "",
    price: "",
    discount: "",
    expireDate: "",
    status: 0,
    showPrice: false,
  },
})

const interfaceList = ref([
  {
    id: "",
    name: "",
  },
])

function init() {
  visible.value = true
  if (form.data.id != "") {
    idQueryPackageData(form.data.id).then((result) => {
      form.data.id = result.data.id
      form.data.interfaceInfoId = result.data.interfaceInfoId
      form.data.name = result.data.name
      form.data.invokeCount = result.data.invokeCount
      form.data.price = result.data.price
      form.data.discount = result.data.discount
      form.data.expireDate = result.data.expireDate
      form.data.status = result.data.status
      form.data.showPrice = result.data.showPrice
    })
  }
}

/**
 * 获取接口列表
 */
function getInterfaceList() {
  queryList().then((result) => {
    interfaceList.value = result.data
  })
}

function close() {
  visible.value = false
  // 重置表单
  form.data = initObjData(dataStructure, "", { status: 0, showPrice: false })
}

function submit() {
  if (form.data.id == "") {
    savePackage(form.data).then((result) => {
      if (result.code != 0) {
        ElMessage.error(result.msg)
      }
      ElMessage.success("添加成功")
      close()
    })
  } else {
    updatePackage(form.data).then((result) => {
      if (result.code != 0) {
        ElMessage.error(result.msg)
      }
      ElMessage.success("修改成功")
      close()
    })
  }
}

getInterfaceList()
defineExpose({ init, form })
</script>
