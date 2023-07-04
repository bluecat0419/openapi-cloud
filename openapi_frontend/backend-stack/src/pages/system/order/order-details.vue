<template>
  <el-dialog v-model="visible" title="详情" :close-on-press-escape="true" :before-close="close">
    <el-form :model="form">
      <el-form-item label="订单号" label-width="120px">
        <el-input v-model="form.data.orderNo" disabled></el-input>
      </el-form-item>
      <el-form-item label="交易号" label-width="120px">
        <el-input v-model="form.data.tradeNo" disabled></el-input>
      </el-form-item>
      <el-form-item label="用户名" label-width="120px">
        <el-input v-model="form.data.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="套餐信息" label-width="120px">
        <el-input v-model="form.data.packageInfo" :autosize="{ minRows: 2, maxRows: 4 }" type="textarea" disabled />
      </el-form-item>
      <el-form-item label="套餐购买数量" label-width="120px">
        <el-input v-model="form.data.count" disabled></el-input>
      </el-form-item>
      <el-form-item label="总金额" label-width="120px">
        <el-input v-model="form.data.totalPrice" disabled></el-input>
      </el-form-item>
      <el-form-item label="支付方式" label-width="120px">
        <el-select v-model="form.data.payType" disabled clearable>
          <el-option label="支付宝" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="支付信息" label-width="120px">
        <el-input v-model="form.data.payInfo" :autosize="{ minRows: 2, maxRows: 4 }" type="textarea" disabled />
      </el-form-item>
      <el-form-item label="退款信息" label-width="120px">
        <el-input v-model="form.data.refundInfo" :autosize="{ minRows: 2, maxRows: 4 }" type="textarea" disabled />
      </el-form-item>
      <el-form-item label="支付时间" label-width="120px">
        <el-input v-model="form.data.paymentDate" disabled></el-input>
      </el-form-item>
      <el-form-item label="状态" label-width="120px">
        <el-select v-model="form.data.status" disabled>
          <el-option label="订单已退款" :value="-1" />
          <el-option label="订单提交" :value="0" />
          <el-option label="支付成功" :value="1" />
          <el-option label="订单超时" :value="2" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref } from "vue"
import { idQueryOrderData } from "@/api/system/order"
import { dataStructure } from "./order"
import { initObjData } from "@/utils/openapi"

const visible = ref(false)
const form = reactive({
  data: {
    id: "",
    orderNo: "",
    tradeNo: "",
    userId: "",
    username: "",
    interfacePackageId: "",
    packageInfo: "",
    count: "",
    totalPrice: "",
    payType: "",
    payInfo: "",
    refundInfo: "",
    status: "",
    paymentDate: "",
    isDeleted: "",
  },
})

function init() {
  visible.value = true
  idQueryOrderData(form.data.id).then((result) => {
    form.data = result.data
  })
}

function close() {
  visible.value = false
  // 重置表单
  form.data = initObjData(dataStructure)
}

defineExpose({ init, form })
</script>
