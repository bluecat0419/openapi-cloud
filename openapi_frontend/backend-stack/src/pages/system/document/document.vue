<template>
  <div class="app-container system-docs">
    <div class="docs-header">
      <el-form :inline="true">
        <el-form-item label="文档标题">
          <el-input v-model="docsTitle" placeholder="请输入文档标题" type="text" clearable />
        </el-form-item>
        <el-form-item label=" " class="docs-header__options">
          <el-button type="primary" @click="getDocumentData">
            <el-icon><Search /></el-icon>
            &nbsp;搜索
          </el-button>
        </el-form-item>
        <el-form-item label="" class="docs-header__options">
          <el-button @click="reset">
            <el-icon><Refresh /></el-icon>
            &nbsp;重置
          </el-button>
        </el-form-item>
        <el-form-item label="" class="docs-header__options">
          <el-button type="success" @click="createDocs">
            <el-icon><CirclePlusFilled /></el-icon>
            &nbsp;添加文档
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- TODO:
      1.表格 需要对sort字段进行排序
    -->
    <el-table
      class="docs-table"
      ref="multipleTableRef"
      :data="docsTable"
      border
      stripe
      :empty-text="空"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        property="title"
        label="文档标题"
        header-align="center"
        width="500"
        align="center"
        show-overflow-tooltip
      />
      <el-table-column property="updateDate" label="更新日期" header-align="center" width="200" />
      <el-table-column property="createDate" label="创建时间" header-align="center" width="200" align="center" />
      <el-table-column fixed="right" header-align="center" label="操作" width="200" align="center">
        <template #default="scope">
          <el-button size="small" type="primary" @click="previewDocs(scope.row.id)">预览</el-button>
          <el-button size="small" type="info" @click="editorDocs(scope.row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="docs-pagination">
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
      ></el-pagination>
    </div>
    <!-- TODO:
      文档标题字数限制在50个字以内
      2.需要做一下插入图片的功能（将图片上传图库，将图库的图片地址插入文档）
      3.这里需要做一下持久化，以保证页面刷新不会将弹框关闭
      4.文档标题和文档内容需要做到好区分
    -->
    <component :is="docsDialog" ref="onDocsDialog" :ctx="docs" @update:data="updateDocsCtx" />
  </div>
</template>

<script setup>
import {
  pagingQuerySystemDocument,
  idQuerySystemDocument,
  deleteSystemDocument,
  updateSystemDocument,
} from "@/api/system/document"
import { OnEditor, OnIncrease, onPreview } from "./dialog/common"
import { ElMessage } from "element-plus"
import { nextTick } from "vue"

const docsTitle = ref("")
const docsTable = ref([])
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
const docs = reactive({
  id: "",
  title: "",
  content: "",
})
const docsDialog = ref("")
const onDocsDialog = ref(null)

/**
 * 查询分页系统文档数据
 */
function getDocumentData() {
  pagingQuerySystemDocument(pagingParameters.page, pagingParameters.pageSize, docsTitle.value).then((res) => {
    pagingParameters.total = res.data.total
    docsTable.value = res.data.list
  })
}

/**
 * 通过id 来获取文档内容
 */
function getDocsData(id) {
  return idQuerySystemDocument(id).then((res) => {
    const { title, content } = res.data
    docs.id = id
    docs.title = title
    docs.content = content
  })
}

/**
 * 头部的按钮方法
 */
function reset() {
  docsTitle.value = ""
  getDocumentData()
}

function createDocs() {
  docsDialog.value = OnIncrease
  nextTick(() => {
    onDocsDialog.value.openDialog()
  })
}

/**
 * 表格发生改变触发的方法
 */
function handleSelectionChange(value) {
  multipleSelection.value = value
}

function handleSizeChange() {
  getDocumentData()
}

function handleCurrentChange() {
  getDocumentData()
}

/**
 * 表格右侧按钮的方法
 */
async function editorDocs(id) {
  await getDocsData(id)
  docsDialog.value = OnEditor
  // 获取数据
  nextTick(() => {
    onDocsDialog.value.openDialog()
  })
}

async function previewDocs(id) {
  await getDocsData(id)
  docsDialog.value = onPreview
  nextTick(() => {
    onDocsDialog.value.openDialog()
  })
}

function updateDocsCtx(data) {
  const sendReq = ref(false)
  const { id, title, content } = data
  for (let key of Object.keys(docs)) {
    if (data[key] !== docs[key]) {
      sendReq.value = true
      break
    }
  }
  if (sendReq.value) {
    updateSystemDocument({ id, title, content }).then((res) => {
      if (res.code === 0) {
        ElMessage({
          message: "修改文档成功",
          type: "success",
        })
        docs.title = title
        docs.content = content
      }
    })
  }
}

function handleDelete(id) {
  ElMessage.confirm(
    "是否确认删除?",
    "Warning",
    {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning",
    }.then(() => {
      deleteSystemDocument(id).then((res) => {
        if (res.code === 0) {
          ElMessage.success("删除成功")
          getDocumentData()
        }
      })
    }),
  )
}

getDocumentData()
</script>

<style lang="scss" scoped>
.system-docs {
  min-height: calc(100vh - 50px);

  .docs-header {
    margin-bottom: 5px;

    .el-input {
      width: 300px;
    }

    .docs-header__options {
      margin-right: 10px;
      &:last-child {
        margin-right: 0;
      }
    }
  }

  .docs-pagination {
    .el-pagination {
      justify-content: center;
    }
  }
}
</style>
