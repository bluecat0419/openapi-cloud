<template>
  <div class="pay_container">
    <el-steps :active="activeStep" finish-status="success" align-center style="margin-bottom: 40px">
      <el-step title="确认订单" />
      <el-step title="支付订单" />
      <el-step title="支付成功" />
    </el-steps>
    <div class="step0_box" v-if="activeStep == 0">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>我的订单</span>
          </div>
        </template>
        <el-table :data="payData.data" style="width: 100%">
          <el-table-column
            :prop="it.prop"
            :label="it.label"
            v-for="(it, idx) in tableColumn"
            :key="idx"
            align="center"
          >
            <template #default="scope" v-if="it.template">
              <div style="align-items: center">
                <div>{{ scope.row.interfaceName }}</div>
                <div style="margin-left: 10px">{{ scope.row.packageName }}</div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <div class="step0_bottom">
        <span>¥{{ payData.data[0]?.price }}</span>
        <el-button type="warning" @click="goToPay">前往支付</el-button>
      </div>
    </div>
    <div v-else-if="activeStep == 1">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>待支付订单</span>
          </div>
        </template>
        <el-table :data="payData.data" style="width: 100%">
          <el-table-column
            :prop="it.prop"
            :label="it.label"
            v-for="(it, idx) in tableColumn"
            :key="idx"
            align="center"
          >
            <template #default="scope" v-if="it.template">
              <div style="align-items: center">
                <div>{{ scope.row.interfaceName }}</div>
                <div style="margin-left: 10px">{{ scope.row.packageName }}</div>
              </div>
            </template>
            <template #default="scope" v-if="it.prop === 'orderId'">
              <div style="align-items: center">
                {{ orderNumber }}
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>打开支付宝扫码支付</span>
          </div>
        </template>
        <div id="qrCodeBox" class="qrcode"></div>
      </el-card>
    </div>
    <div v-else class="step2_box">
      <el-icon class="step2_box_icon"><Select /></el-icon>
      <span class="step2_box_text">恭喜，支付成功！</span>
      <div class="button_box">
        <el-button type="primary" plain @click="goHome">回首页</el-button>
        <el-button type="warning" plain @click="goOrderDetail">查看订单</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { generateOrder, createOrder, getPayQrCode, checkPay } from "@/api/order"
import QRCode from "qrcodejs2-fix" //在需要使用的vue文件中导入即可
const router = useRouter()
const route = useRoute()
let { interfacePackageId, count } = route.query

//步骤切换
const activeStep = ref(0)
//购买信息确认
const tableColumn = computed(() => {
  let step1Column = [
    {
      label: "产品名称",
      template: true,
    },
    {
      prop: "count",
      label: "数量",
    },
    {
      prop: "discount",
      label: "折扣",
    },
    {
      prop: "price",
      label: "资费",
    },
  ]
  return activeStep.value === 0
    ? step1Column
    : [{ prop: "orderId", label: "订单号" }, ...step1Column]
})
const payData = reactive({
  data: [],
})
async function getGenerateOrder() {
  let { data } = await generateOrder({ count, interfacePackageId })
  payData.data = [data]
}
getGenerateOrder()
//支付
let orderNumber = ref("")
let payCode = ref("")
let payStatus = ref(false)
let timer = null
function creatQrCode(code) {
  document.getElementById("qrCodeBox").innerHTML = ""
  new QRCode(document.getElementById("qrCodeBox"), {
    text: code,
    width: 200, //二维码宽
    height: 200, //二维码高
  })
}
async function goToPay() {
  let { data } = await createOrder({ count, interfacePackageId, payType: 0 })
  orderNumber.value = data
  let result = await handleCheckPay()
  if (!result) {
    payCode.value = await getPayQrCode(data)
    activeStep.value = 1
    nextTick(() => {
      creatQrCode(data)
      timer = setInterval(() => {
        if (payStatus.value === true) return clearInterval(timer)
        handleCheckPay()
      }, 3000)
    })
  }
}
async function handleCheckPay() {
  let { data } = await checkPay(orderNumber.value)
  payStatus.value = data
  if (data) activeStep.value = 2
  return data
}

//支付成功
function goHome() {
  router.push("/")
}
function goOrderDetail() {
  router.push({ path: "/user/order" })
}
onBeforeUnmount(() => {
  clearInterval(timer)
})
</script>

<style lang="scss" scoped>
.pay_container {
  padding: 40px 80px;
  .step0_box {
    .step0_bottom {
      margin: 80px 20px 0 0;
      text-align: right;
      > span {
        margin-right: 20px;
        color: orangered;
      }
    }
  }
  .step2_box {
    text-align: center;
    padding-top: 60px;
    .step2_box_icon {
      color: forestgreen;
      font-size: 50px;
    }
    .step2_box_text {
      font-size: 36px;
    }
    .button_box {
      margin-top: 20px;
    }
  }
}
</style>
