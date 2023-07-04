<template>
  <el-dialog
    v-model="visible"
    :title="form.data.id != '' ? '修改' : '添加'"
    :close-on-press-escape="true"
    :before-close="close"
  >
    <el-form :model="form">
      <el-form-item label="接口名称" label-width="120px" label-height="50px">
        <el-input v-model="form.data.name" placeholder="请输入接口名称"></el-input>
      </el-form-item>
      <el-form-item label="接口图标" label-width="120px">
        <upload-icon :icon="form.data.icon" @update:icon="updateFormIcon"></upload-icon>
      </el-form-item>
      <el-form-item label="接口描述" label-width="120px">
        <el-input
          v-model="form.data.description"
          :autosize="{ minRows: 2, maxRows: 4 }"
          type="textarea"
          placeholder="请输入接口描述"
        />
      </el-form-item>
      <el-form-item label="接口地址" label-width="120px">
        <el-input v-model="form.data.url" placeholder="请输入接口地址"></el-input>
      </el-form-item>
      <el-form-item label="请求类型" label-width="120px">
        <el-select v-model="form.data.method" placeholder="请选择接口请求类型" clearable>
          <el-option label="GET" value="GET" />
          <el-option label="POST" value="POST" />
          <el-option label="PUT" value="PUT" />
          <el-option label="DELETE" value="DELETE" />
        </el-select>
      </el-form-item>
      <el-form-item label="请求参数" label-width="120px">
        <el-table ref="param_table" tooltip-effect="dark" :data="form.data.requestParamList" style="width: 100%">
          <el-table-column label="名称" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.name" placeholder="请输入参数名称"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="类型" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.type" placeholder="请输入参数类型"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="是否必须" width="150">
            <template #default="scope">
              <el-select v-model="scope.row.required" placeholder="请选择是否必填" clearable>
                <el-option label="必选" :value="true" />
                <el-option label="可选" :value="false" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="描述" width="150">
            <template #default="scope">
              <el-input
                v-model="scope.row.description"
                :autosize="{ minRows: 1, maxRows: 3 }"
                type="textarea"
                placeholder="请输入参数描述"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="scope">
              <el-button size="small" type="danger" @click="paramHandleDelete(scope.$index)">删除</el-button>
            </template>

            <template #header>
              <el-button size="small" @click="paramHandleAdd()">添加</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item label="请求体参数" label-width="120px">
        <el-table ref="param_table" tooltip-effect="dark" :data="form.data.requestBodyList" style="width: 100%">
          <el-table-column label="名称" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.name" placeholder="请输入参数名称"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="类型" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.type" placeholder="请输入参数类型"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="是否必须" width="150">
            <template #default="scope">
              <el-select v-model="scope.row.required" placeholder="请选择是否必填" clearable>
                <el-option label="必选" :value="true" />
                <el-option label="可选" :value="false" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="描述" width="150">
            <template #default="scope">
              <el-input
                v-model="scope.row.description"
                :autosize="{ minRows: 1, maxRows: 3 }"
                type="textarea"
                placeholder="请输入参数描述"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #header>
              <el-button size="small" @click="bodyHandleAdd()">添加</el-button>
            </template>

            <template #default="scope">
              <el-button size="small" type="danger" @click="bodyHandleDelete(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item label="请求头参数" label-width="120px">
        <el-table ref="param_table" tooltip-effect="dark" :data="form.data.requestHeaderList" style="width: 100%">
          <el-table-column label="名称" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.name" placeholder="请输入参数名称"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="类型" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.type" placeholder="请输入参数类型"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="是否必须" width="150">
            <template #default="scope">
              <el-select v-model="scope.row.required" placeholder="请选择是否必填" clearable>
                <el-option label="必选" :value="true" />
                <el-option label="可选" :value="false" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="描述" width="150">
            <template #default="scope">
              <el-input
                v-model="scope.row.description"
                :autosize="{ minRows: 1, maxRows: 3 }"
                type="textarea"
                placeholder="请输入参数描述"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="scope">
              <el-button size="small" type="danger" @click="headerHandleDelete(scope.$index)">删除</el-button>
            </template>

            <template #header>
              <el-button size="small" @click="headerHandleAdd()">添加</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item label="返回类型" label-width="120px">
        <el-input v-model="form.data.responseType" placeholder="请输入返回类型"></el-input>
      </el-form-item>
      <el-form-item label="返回数据" label-width="120px">
        <el-input
          v-model="form.data.responseData"
          :autosize="{ minRows: 2, maxRows: 4 }"
          type="textarea"
          placeholder="请输入返回数据"
        />
      </el-form-item>
      <el-form-item label="示例代码" label-width="120px">
        <el-input
          v-model="form.data.codeDemo"
          :autosize="{ minRows: 2, maxRows: 4 }"
          type="textarea"
          placeholder="请输入示例代码"
        />
      </el-form-item>
      <el-form-item label="状态" label-width="120px">
        <el-select v-model="form.data.status" placeholder="请选择状态" clearable>
          <el-option label="开启" :value="1" />
          <el-option label="关闭" :value="0" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button @click="close">取消</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { saveInterface, updateInterface, idQueryInterfaceData } from "@/api/system/interface"
import UploadIcon from "./upload-icon.vue"
import { dataStructure } from "./interface"
import { ElMessage } from "element-plus"
import { initObjData } from "@/utils/openapi"

const visible = ref(false)
const form = reactive({
  data: {
    id: "",
    name: "",
    icon: "",
    description: "",
    url: "",
    requestParamList: [
      {
        name: "",
        type: "",
        required: false,
        description: "",
      },
    ],
    requestBodyList: [
      {
        name: "",
        type: "",
        required: false,
        description: "",
      },
    ],
    requestHeaderList: [
      {
        name: "",
        type: "",
        required: false,
        description: "",
      },
    ],
    responseHeader: "",
    responseType: "",
    responseData: "",
    method: "",
    status: "",
    codeDemo: "",
  },
})

function init() {
  visible.value = true
  if (form.data.id !== "") {
    idQueryInterfaceData(form.data.id).then((result) => {
      form.data = result.data
    })
  }
}

function close() {
  visible.value = false
  // 重置表单
  form.data = initObjData(dataStructure, "", { required: false })
}

function submit() {
  if (form.data.id == "") {
    saveInterface(form.data).then((result) => {
      if (result.code != 0) {
        ElMessage.error(result.msg)
      }
      ElMessage.success("添加成功")
      close()
    })
  } else {
    updateInterface(form.data).then((result) => {
      if (result.code != 0) {
        ElMessage.error(result.msg)
      }
      ElMessage.success("修改成功")
      close()
    })
  }
}

function paramHandleAdd() {
  const copyData = initObjData(dataStructure.requestParamList, "", { required: false })
  if (form.data.requestParamList === null) {
    form.data.requestParamList = copyData
  } else {
    form.data.requestParamList.push(...copyData)
  }
}

function paramHandleDelete(index) {
  console.log("index: ", index)
  form.data.requestParamList.splice(index, 1)
}

function bodyHandleAdd() {
  const copyData = initObjData(dataStructure.requestBodyList, "", { required: false })
  if (form.data.requestBodyList === null) {
    form.data.requestBodyList = copyData
  } else {
    form.data.requestBodyList.push(...copyData)
  }
}

function bodyHandleDelete(index) {
  form.data.requestBodyList.splice(index, 1)
}

function headerHandleAdd() {
  const copyData = initObjData(dataStructure.requestHeaderList, "", { required: false })
  if (form.data.requestHeaderList === null) {
    form.data.requestHeaderList = copyData
  } else {
    form.data.requestHeaderList.push(...copyData)
  }
}

function headerHandleDelete(index) {
  form.data.requestHeaderList.splice(index, 1)
}

/**
 * 子组件方法
 */
function updateFormIcon(url) {
  form.data.icon = url
}
defineExpose({ init, form })
</script>
