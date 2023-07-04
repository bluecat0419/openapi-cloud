<template>
  <div class="app-container system-order">
    <div class="order-header">
      <el-form :inline="true" :model="dataForm">
        <el-form-item label="订单号">
          <el-input v-model="dataForm.orderNo" placeholder="请输入订单号" type="text" clearable />
        </el-form-item>
        <el-form-item label="交易号">
          <el-input v-model="dataForm.tradeNo" placeholder="请输入订单号" type="text" clearable />
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="dataForm.payType" placeholder="请选择支付方式" clearable>
            <el-option label="支付宝" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="dataForm.status" placeholder="请选择状态" clearable>
            <el-option label="订单已退款" value="-1" />
            <el-option label="订单提交" value="0" />
            <el-option label="支付成功" value="1" />
            <el-option label="订单超时" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label=" " :style="{ 'margin-right': '10px' }">
          <el-button :icon="Search" type="primary" @click="getOrderData">搜索</el-button>
        </el-form-item>
        <el-form-item label="" :style="{ 'margin-right': '10px' }">
          <el-button :icon="Refresh" @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      class="order-table"
      ref="multipleTableRef"
      :data="orderTable"
      style="width: 100%"
      border
      stripe
      :empty-text="空"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        property="orderNo"
        label="订单号"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column
        property="tradeNo"
        label="交易号"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column property="totalPrice" label="总金额" header-align="center" width="100" align="center" />
      <el-table-column property="payType" label="支付方式" header-align="center" width="100" align="center">
        <template #default="scope">
          {{ scope.row.payType == 0 ? "支付宝" : "xx" }}
        </template>
      </el-table-column>
      <el-table-column property="status" label="订单状态" header-align="center" width="180" align="center">
        <template #default="scope">
          <span v-if="scope.row.status == -1" style="color: #c03639">已退款</span>
          <span v-if="scope.row.status == 0" style="color: #e6a23c">待支付</span>
          <span v-if="scope.row.status == 1" style="color: #67c23a">支付成功</span>
          <span v-if="scope.row.status == 2" style="color: blueviolet">已过期</span>
        </template>
      </el-table-column>
      <el-table-column property="paymentDate" label="付款时间" header-align="center" width="180" align="center" />
      <el-table-column fixed="right" header-align="center" label="操作" width="150" align="center">
        <template #default="scope">
          <el-button size="small" @click="orderDetailsHandle(scope.row.id)">详情</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="order-pagination">
      <el-pagination
        v-model:current-page="pagingParameters.page"
        v-model:page-size="pagingParameters.pageSize"
        :page-sizes="pagingParameters.pageSizes"
        :small="small"
        :disabled="disabled"
        :background="background"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagingParameters.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <OrderDetails ref="orderDetails"></OrderDetails>
  </div>
</template>

<script setup>
import { pagingQueryOrderData, deleteOrder } from "@/api/system/order"
import { timestampConversion } from "@/utils/time"
import { initObjData } from "@/utils/openapi"
import { filterStructure } from "./order"
import { Search, Refresh, CirclePlusFilled } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox } from "element-plus"
import OrderDetails from "./order-details.vue"

const orderTable = ref([])
const multipleTableRef = ref()
const multipleSelection = ref([])
const pagingParameters = reactive({
  page: 1,
  pageSize: 10,
  pageSizes: [10, 20, 30, 50],
  total: 0,
  get totalPage() {
    return Math.ceil(this.total / this.pageSize)
  },
})
const dataForm = ref({
  orderNo: "",
  tradeNo: "",
  payType: "",
  status: "",
  paymentStartDate: "",
  paymentEndDate: "",
})

const orderDetails = ref(null)

/**
 * 查询分页用户数据
 */
function getOrderData() {
  pagingQueryOrderData(pagingParameters.page, pagingParameters.pageSize, dataForm.value).then((result) => {
    pagingParameters.total = result.data.total
    orderTable.value = result.data.list
  })
}

/**
 * 重置
 */
function reset() {
  dataForm.value = initObjData(filterStructure)
  getOrderData()
}

function handleSelectionChange(value) {
  multipleSelection.value = value
}

function handleSizeChange() {
  getOrderData()
}

function handleCurrentChange() {
  getOrderData()
}

function orderDetailsHandle(id) {
  nextTick(() => {
    orderDetails.value.form.data.id = id
    orderDetails.value.init()
  })
}

function handleDelete(id) {
  ElMessageBox.confirm("是否确认删除?", "Warning", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    deleteOrder(id).then((result) => {
      if (result.code != 0) {
        ElMessage.error(result.msg)
      }
      ElMessage.success("删除成功")
      getOrderData()
    })
  })
}

getOrderData()
</script>

<style lang="scss" scoped>
.system-order {
  min-height: calc(100vh - 50px);

  .order-header {
    margin-bottom: 5px;

    .el-input {
      width: 150px;
    }
  }

  .order-pagination {
    .el-pagination {
      justify-content: center;
    }
  }
}
</style>
