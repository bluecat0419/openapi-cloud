<template>
  <div class="app-container system-package">
    <div class="package-header">
      <el-form :inline="true" :model="dataForm">
        <el-form-item label="接口列表">
          <el-select v-model="dataForm.interfaceInfoId" placeholder="请选择接口" filterable clearable>
            <el-option v-for="item in interfaceList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="套餐名称">
          <el-input v-model="dataForm.packageName" placeholder="请输入套餐名称" type="text" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="dataForm.status" placeholder="请选择状态" clearable>
            <el-option label="可用" value="0" />
            <el-option label="不可用" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label=" " :style="{ 'margin-right': '10px' }">
          <el-button :icon="Search" type="primary" @click="getPackageData">搜索</el-button>
        </el-form-item>
        <el-form-item label="" :style="{ 'margin-right': '10px' }">
          <el-button :icon="Refresh" @click="reset">重置</el-button>
        </el-form-item>
        <el-form-item label="" :style="{ 'margin-right': '10px' }">
          <el-button :icon="CirclePlusFilled" type="primary" @click="saveOrUpdateHandle('')">添加</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      class="package-table"
      ref="multipleTableRef"
      :data="packageTable"
      style="width: 100%"
      border
      stripe
      :empty-text="空"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        property="name"
        label="套餐名称"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column property="invokeCount" label="接口调用次数" header-align="center" width="180" align="center" />
      <el-table-column property="price" label="价格" header-align="center" width="180" align="center" />
      <el-table-column property="discount" label="折扣" header-align="center" width="180" align="center" />
      <el-table-column property="expireDate" label="过期时间" header-align="center" width="180" align="center" />
      <el-table-column
        property="status"
        label="状态"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      >
        <template #default="scope">
          {{ scope.row.status == 0 ? "可用" : "不可用" }}
        </template>
      </el-table-column>

      <el-table-column fixed="right" header-align="center" label="操作" width="150" align="center">
        <template #default="scope">
          <el-button size="small" @click="saveOrUpdateHandle(scope.row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="package-pagination">
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
    <SaveOrUpdate ref="saveOrUpdate"></SaveOrUpdate>
  </div>
</template>

<script setup>
import { pagingQueryPackageData, deletePackage } from "@/api/system/package"
import { queryList } from "@/api/system/interface"
import { initObjData } from "@/utils/openapi"
import { filterStructure } from "./package"
import { Search, Refresh, CirclePlusFilled } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox } from "element-plus"
import SaveOrUpdate from "./package-save-or-update.vue"

const packageTable = ref([])
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
  interfaceInfoId: "",
  packageName: "",
  status: "",
})

const interfaceList = ref([
  {
    id: "",
    name: "",
  },
])

const saveOrUpdate = ref(null)

/**
 * 查询分页用户数据
 */
function getPackageData() {
  pagingQueryPackageData(pagingParameters.page, pagingParameters.pageSize, dataForm.value).then((result) => {
    pagingParameters.total = result.data.total
    packageTable.value = result.data.list
  })
}

/**
 * 获取接口列表
 */
function getInterfaceList() {
  queryList().then((result) => {
    interfaceList.value = result.data
  })
}

/**
 * 重置
 */
function reset() {
  dataForm.value = initObjData(filterStructure)
  getPackageData()
}

function handleSelectionChange(value) {
  multipleSelection.value = value
}

function handleSizeChange() {
  getPackageData()
}

function handleCurrentChange() {
  getPackageData()
}

function saveOrUpdateHandle(id) {
  nextTick(() => {
    saveOrUpdate.value.form.data.id = id
    saveOrUpdate.value.init()
  })
}

function handleDelete(id) {
  ElMessageBox.confirm("是否确认删除?", "Warning", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    deletePackage(id).then((result) => {
      if (result.code != 0) {
        ElMessage.error(result.msg)
      }
      ElMessage.success("删除成功")
      getPackageData()
    })
  })
}

getPackageData()
getInterfaceList()
</script>

<style lang="scss" scoped>
.system-package {
  min-height: calc(100vh - 50px);

  .package-header {
    margin-bottom: 5px;

    .el-input {
      width: 150px;
    }
  }

  .package-pagination {
    .el-pagination {
      justify-content: center;
    }
  }
}
</style>
