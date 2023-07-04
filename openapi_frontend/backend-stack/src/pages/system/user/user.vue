<template>
  <div class="app-container system-user">
    <div class="user-header">
      <el-form :inline="true" :model="dataForm">
        <el-form-item label="用户名">
          <el-input v-model="dataForm.username" placeholder="请输入用户名" type="text" clearable />
        </el-form-item>
        <el-form-item label="真实名称">
          <el-input v-model="dataForm.realName" placeholder="请输入真实名称" type="text" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="dataForm.mobile" placeholder="请输入手机号" type="text" clearable />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="dataForm.email" placeholder="请输入邮箱" type="text" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="dataForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" value="1" />
            <el-option label="停用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label=" " :style="{ 'margin-right': '10px' }">
          <el-button :icon="Search" type="primary" @click="getUserData">搜索</el-button>
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
      class="user-table"
      ref="multipleTableRef"
      :data="userTable"
      style="width: 100%"
      border
      stripe
      :empty-text="空"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        property="username"
        label="用户名"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column
        property="realName"
        label="真实名称"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column property="gender" label="性别" header-align="center" width="80" align="center">
        <template #default="scope">
          {{ scope.row.gender == 0 ? "男" : "女" }}
        </template>
      </el-table-column>
      <el-table-column
        property="mobile"
        label="手机号"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column
        property="email"
        label="邮箱"
        header-align="center"
        width="180"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column property="status" label="状态" header-align="center" width="80" align="center">
        <template #default="scope">
          {{ scope.row.status == 0 ? "停用" : "正常" }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" header-align="center" width="180" align="center">
        <template #default="scope">{{ timestampConversion(scope.row.createDate) }}</template>
      </el-table-column>
      <el-table-column fixed="right" header-align="center" label="操作" width="150" align="center">
        <template #default="scope">
          <el-button size="small" @click="saveOrUpdateHandle(scope.row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="user-pagination">
      <el-pagination
        v-model:current-page="pagingParameters.page"
        v-model:page-size="pagingParameters.pageSize"
        :page-sizes="pagingParameters.pageSizes"
        :small="small"
        :disabled="disabled"
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
import { pagingQueryUserData, deleteUser } from "@/api/system/user"
import { initObjData } from "@/utils/openapi"
import { timestampConversion } from "@/utils/time"
import { filterStructure } from "./user"
import { Search, Refresh, CirclePlusFilled } from "@element-plus/icons-vue"
import SaveOrUpdate from "./user-save-or-update.vue"
import { ElMessage, ElMessageBox } from "element-plus"
import { ref, reactive, nextTick } from "vue"

const userTable = ref([])
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
  username: "",
  realName: "",
  mobile: "",
  email: "",
  status: "",
})

const saveOrUpdate = ref(null)

/**
 * 查询分页用户数据
 */
function getUserData() {
  pagingQueryUserData(pagingParameters.page, pagingParameters.pageSize, dataForm.value).then((result) => {
    pagingParameters.total = result.data.total
    userTable.value = result.data.list
  })
}

/**
 * 重置
 */
function reset() {
  dataForm.value = initObjData(filterStructure)
  getUserData()
}

function handleSelectionChange(value) {
  multipleSelection.value = value
}

function handleSizeChange() {
  getUserData()
}

function handleCurrentChange() {
  getUserData()
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
    deleteUser(id).then((result) => {
      if (result.code === 0) {
        ElMessage.success("删除成功")
        getUserData()
      }
    })
  })
}

getUserData()
</script>

<style lang="scss" scoped>
.system-user {
  min-height: calc(100vh - 50px);

  .user-header {
    margin-bottom: 5px;

    .el-input {
      width: 150px;
    }
  }

  .user-pagination {
    .el-pagination {
      justify-content: center;
    }
  }
}
</style>
