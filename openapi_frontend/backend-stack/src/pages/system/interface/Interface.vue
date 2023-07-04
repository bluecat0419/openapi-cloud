<template>
  <div class="app-container system-interface">
    <div class="interface-header">
      <el-form :inline="true" :model="dataForm">
        <el-form-item label="接口名称">
          <el-input v-model="dataForm.name" placeholder="请输入接口名称" type="text" clearable />
        </el-form-item>
        <el-form-item label="接口地址">
          <el-input v-model="dataForm.url" placeholder="请输入接口地址" type="text" clearable />
        </el-form-item>
        <el-form-item label="请求类型">
          <el-select v-model="dataForm.method" placeholder="请选择接口请求类型" clearable>
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
            <el-option label="PUT" value="PUT" />
            <el-option label="DELETE" value="DELETE" />
          </el-select>
        </el-form-item>
        <el-form-item label="响应类型">
          <el-input v-model="dataForm.responseType" placeholder="请输入响应类型" type="text" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="dataForm.status" placeholder="请选择状态" clearable>
            <el-option label="开启" value="1" />
            <el-option label="关闭" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label=" " :style="{ 'margin-right': '10px' }">
          <el-button :icon="Search" type="primary" @click="getInterfaceData">搜索</el-button>
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
      class="interface-table"
      ref="multipleTableRef"
      :data="interfaceTable"
      style="width: 100%"
      border
      stripe
      :empty-text="空"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        property="name"
        label="接口名称"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column
        property="url"
        label="接口地址"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column property="method" label="请求类型" header-align="center" width="180" align="center" />
      <el-table-column
        property="responseType"
        label="响应类型"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column
        property="status"
        label="状态"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      >
        <template #default="scope">
          {{ scope.row.status == 0 ? "关闭" : "开启" }}
        </template>
      </el-table-column>

      <el-table-column fixed="right" header-align="center" label="操作" width="150" align="center">
        <template #default="scope">
          <el-button size="small" @click="saveOrUpdateHandle(scope.row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="interface-pagination">
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
import { pagingQueryInterfaceData, deleteInterface } from "@/api/system/interface"
import { initObjData } from "@/utils/openapi"
import { Search, Refresh, CirclePlusFilled } from "@element-plus/icons-vue"
import { ElMessage, ElMessageBox } from "element-plus"
import { filterStructure } from "./interface"
import SaveOrUpdate from "./interface-save-or-update.vue"

const interfaceTable = ref([])
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
  name: "",
  url: "",
  method: "",
  responseType: "",
  status: "",
})

const saveOrUpdate = ref(null)

/**
 * 查询分页接口数据
 */
function getInterfaceData() {
  pagingQueryInterfaceData(pagingParameters.page, pagingParameters.pageSize, dataForm.value).then((result) => {
    pagingParameters.total = result.data.total
    interfaceTable.value = result.data.list
  })
}

/**
 * 重置
 */
function reset() {
  dataForm.value = initObjData(filterStructure)
  getInterfaceData()
}

function handleSelectionChange(value) {
  multipleSelection.value = value
}

function handleSizeChange() {
  getInterfaceData()
}

function handleCurrentChange() {
  getInterfaceData()
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
    deleteInterface(id).then((result) => {
      if (result.code === 0) {
        ElMessage.success("删除成功")
        getInterfaceData()
      }
    })
  })
}

getInterfaceData()
</script>

<style lang="scss" scoped>
.system-interface {
  min-height: calc(100vh - 50px);

  .interface-header {
    margin-bottom: 5px;

    .el-input {
      width: 150px;
    }
  }

  .interface-pagination {
    .el-pagination {
      justify-content: center;
    }
  }
}
</style>
