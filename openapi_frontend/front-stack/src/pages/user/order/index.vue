<template>
  <div class="user-order">
    <div style="margin-bottom: 20px">我的订单</div>

    <el-empty v-if="orderListData.data.length == 0" description="暂无订单" />
    <div v-else>
      <el-card
        class="box-card card_item"
        v-for="(it, idx) in orderListData.data"
        :key="idx"
        shadow="never"
      >
        <template #header>
          <div class="card-header">
            <span>订单号：{{ it.orderNo }}</span>
          </div>
        </template>
        <div class="text item card_content">
          <div class="card_left">
            <img
              :src="it.icon || interfaceImg"
              alt=""
              style="width: 60px; height: 60px; margin-right: 6px"
            />
            <div class="card_left_text">
              <div>{{ it.interfaceName }}</div>
              <span>{{ it.packageName }}</span>
            </div>
          </div>
          <div class="card_right">
            <span>数量：x{{ it.count }}</span>
            <span>金额：{{ it.price }}</span>
            <!-- style="width: 120px;text-align: center;" -->
            <div>
              <el-tag v-if="it.status === 0">订单已提交</el-tag>
              <div v-else-if="it.status === 1">
                <el-tag type="success">支付成功</el-tag>
                <el-tag type="danger" @click="handleRefund(it)" v-if="canRefund(it)">
                  申请退款
                </el-tag>
              </div>
              <el-tag v-else-if="it.status === 2" type="info">订单已过期</el-tag>
              <el-tag v-else type="info">订单已退款</el-tag>
            </div>
            <el-button class="button"  @click="openDrawer(it.orderId)">
              查看详情
            </el-button>
          </div>
        </div>
      </el-card>
      <Pagination
        :current="paginationData.current"
        :total="paginationData.total"
        @changePage="handleChangePage"
      ></Pagination>
    </div>
    <el-drawer v-model="orderDetailDrawer" direction="rtl" :before-close="handleDrawerClose">
      <el-descriptions class="margin-top" title="订单详情" :column="1" size="large" border>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">订单号</div>
          </template>
          {{ orderDetail.data.orderNo || "--" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">交易号</div>
          </template>
          {{ orderDetail.data.tradeNo || "--" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">接口名称</div>
          </template>
          {{ orderDetail.data.interfaceName || "--" }}
        </el-descriptions-item>
        <!-- <el-descriptions-item>
          <template #label>
            <div class="cell-item">图标</div>
          </template>
          {{ orderDetail.data.icon || "--" }}
        </el-descriptions-item> -->
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">套餐名称</div>
          </template>
          {{ orderDetail.data.packageName || "--" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">数量</div>
          </template>
          {{ orderDetail.data.count || "--" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">总价格</div>
          </template>
          {{ orderDetail.data.price || "--" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">折扣</div>
          </template>
          {{ orderDetail.data.discount || "--" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">实付款</div>
          </template>
          {{ orderDetail.data.realPrice || "--" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">创建时间</div>
          </template>
          {{ orderDetail.data.createDate || "--" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">付款时间</div>
          </template>
          {{ orderDetail.data.paymentDate || "--" }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">订单状态</div>
          </template>
          <el-tag v-if="orderDetail.data.status === 0">订单已提交</el-tag>
          <el-tag type="success" v-else-if="orderDetail.data.status === 1">支付成功</el-tag>
          <el-tag v-else-if="orderDetail.data.status === 2" type="info">订单已过期</el-tag>
          <el-tag v-else type="info">订单已退款</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-drawer>
    <el-dialog
      v-model="refundDialogVisible"
      title="申请退款"
      width="30%"
      :before-close="handleDialogClose"
    >
      <el-form label-position="top" ref="ruleFormRef" :model="refundForm" :rules="refundRules">
        <el-form-item label="信息确认">
          <div>
            <div>订单号：{{ refundOrder.data.orderId }}</div>
            <div>
              商品名称:{{ refundOrder.data.interfaceName }}--{{ refundOrder.data.packageName }}
            </div>
            <div>退款金额:{{ refundOrder.data.price }}</div>
          </div>
        </el-form-item>
        <el-form-item label="退款原因" required prop="refundReason">
          <el-input v-model="refundForm.refundReason" placeholder="请输入退款原因"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="refundDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="refundSubmit(ruleFormRef)">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import interfaceImg from "@/assets/images/profile.jpg"
import { myOrderPage, myOrderDetail, orderRefund } from "@/api/user/order.js"
import Pagination from "@/components/Pagination/index.vue"
import { ElMessage } from "element-plus"
const router = useRouter()
//订单分页
onMounted(() => {
  getOrderPage()
})
let paginationData = reactive({
  current: "1",
  total: "1",
  limit: 8,
})
let orderListData = reactive({
  data: [],
})
async function getOrderPage() {
  let { data } = await myOrderPage({ current: paginationData.current, limit: paginationData.limit })
  orderListData.data = data.list
  paginationData.total = Math.ceil(data.total / paginationData.limit)
}
function handleChangePage(page) {
  paginationData.current = page
  getOrderPage()
}
function canRefund(it) {
  let time = new Date(it.paymentDate).getTime() + 7 * 24 * 3600 * 1000
  let now = new Date().getTime()
  if (it.price != "0" && now < time) {
    return true
  } else return false
}
//订单详情
let orderDetailDrawer = ref(false)
let orderDetail = reactive({
  data: {},
})
function openDrawer(id) {
  getOrderDetail(id)
  orderDetailDrawer.value = true
}
async function getOrderDetail(id) {
  let { data } = await myOrderDetail(id)
  orderDetail.data = data
}
function handleDrawerClose(done) {
  done()
}
//申请退款
let refundDialogVisible = ref(false)
let refundOrder = reactive({
  data: {},
})
let refundForm = reactive({
  refundReason: "",
})
let refundRules = {
  refundReason: [{ required: true, message: "请输入退款原因", trigger: "blur" }],
}
const ruleFormRef = ref()
function resetForm(formEl) {
  if (!formEl) return
  formEl.resetFields()
}
function handleRefund(it) {
  refundOrder.data = it
  refundDialogVisible.value = true
  resetForm(ruleFormRef)
}
async function refundSubmit(formEl) {
  if (!formEl) return
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      let { data } = await orderRefund({
        id: refundOrder.data.orderId,
        refundReason: refundForm.refundReason,
      })
      if (data) ElMessage({ message: "退款成功", type: "success" })
    } else {
      console.log("error submit!", fields)
    }
  })
}
function handleDialogClose(done) {
  done()
}
</script>

<style lang="scss" scoped>
.user-order {
  margin: 5px 40px;
  min-height: calc(100vh - 50px);
  .card_item {
    ::v-deep .el-card__header {
      background-color: #eee;
    }
    .card_content {
      display: flex;
      .card_left {
        display: flex;
        width: 200px;
        .card_left_text {
          display: flex;
          flex-direction: column;
          justify-content: space-around;
        }
      }
      .card_right {
        padding-top: 20px;
        flex: 1;
        display: flex;
        justify-content: space-around;
      }
    }
  }
}
</style>
