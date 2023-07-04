<template>
  <div class="interface-info app-container">
    <div class="interface-table public_own" style="margin: 15px">
      <el-descriptions
        class="margin-top"
        :title="interfaceDetail.detail.name"
        :column="1"
        size="large"
        :border="true"
      >
        <el-descriptions-item label-align="center">
          <template #label>
            <div class="cell-item">描述</div>
          </template>
          <span>{{ interfaceDetail.detail.description }}</span>
        </el-descriptions-item>
        <el-descriptions-item label-align="center">
          <template #label>
            <div class="cell-item">成交量</div>
          </template>
          <span>{{ interfaceDetail.detail.dealCount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label-align="center">
          <template #label>
            <div class="cell-item">评分</div>
          </template>
          <el-rate
            v-model="interfaceDetail.detail.score"
            size="small"
            class="interface_rate"
            disabled
          ></el-rate>
        </el-descriptions-item>
        <el-descriptions-item label-align="center">
          <template #label>
            <div class="cell-item">套餐</div>
          </template>
          <el-radio-group
            v-model="interfaceDetail.selectPackage.packageId"
            v-if="interfaceDetail.detail.packages && interfaceDetail.detail.packages.length > 0"
          >
            <el-radio
              :label="it.packageId"
              v-for="(it, idx) in interfaceDetail.detail.packages"
              @click="handleChangePackage(idx)"
            >
              {{ it.packageName }}
            </el-radio>
          </el-radio-group>
          <span v-else>--</span>
        </el-descriptions-item>
        <el-descriptions-item label-align="center">
          <template #label>
            <div class="cell-item">价格</div>
          </template>
          <span style="display: inline-block; margin-right: 20px; width: 100px">
            {{ interfaceDetail.selectPackage?.price || "--" }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label-align="center">
          <template #label>
            <div class="cell-item">购买数量</div>
          </template>
          <el-input-number
            v-model="purchaseNum"
            :disabled="
              !interfaceDetail.detail.packages || interfaceDetail.detail.packages.length == 0
            "
            :min="1"
            style="margin-right: 20px"
          />
          <el-button
            type="danger"
            @click="goOrderPay"
            :disabled="
              !interfaceDetail.detail.packages || interfaceDetail.detail.packages.length == 0
            "
          >
            立即购买
          </el-button>
        </el-descriptions-item>
      </el-descriptions>
    </div>
    <el-menu
      :default-active="activeMenuIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleMenuSelect"
      style="margin-top: 30px"
    >
      <el-menu-item index="1">API文档</el-menu-item>
      <el-menu-item index="2">在线调试</el-menu-item>
      <el-menu-item index="3">用户评论</el-menu-item>
    </el-menu>
    <div class="interfaceDoc public_own" v-if="activeMenuIndex === '1'">
      <div class="interfaceDoc_header">{{ interfaceDocData.data.name }}</div>
      <div class="header_item">
        <span class="item_lable">调用地址：</span>
        <span class="item_content invokeUrl">{{ interfaceDocData.data.url }}</span>
      </div>
      <div class="header_item">
        <span class="item_lable">请求方式：</span>
        <span class="item_content">{{ interfaceDocData.data.method }}</span>
      </div>
      <div class="header_item">
        <span class="item_lable">返回类型：</span>
        <span class="item_content">{{ interfaceDocData.data.responseType }}</span>
      </div>
      <div class="demo-collapse">
        <el-collapse v-model="collapseActiveIndex" @change="handleCollapseChange">
          <el-collapse-item title="请求参数（Headers）" name="1">
            <el-table
              :data="interfaceDocData.data.requestHeaderList"
              stripe
              style="width: 100%"
              v-show="interfaceDocData.data.requestHeaderList?.length > 0"
            >
              <el-table-column
                :prop="it.prop"
                :label="it.lable"
                :formatter="it.formmator"
                v-for="it in interfaceDocColumns"
                align="center"
              />
            </el-table>
            <div
              v-show="!(interfaceDocData.data.requestHeaderList?.length > 0)"
              style="text-align: center"
            >
              <el-icon><RemoveFilled /></el-icon>
              无参数
            </div>
          </el-collapse-item>
          <el-collapse-item title="请求参数（Params）" name="2">
            <el-table
              :data="interfaceDocData.data.requestParamsList"
              stripe
              style="width: 100%"
              v-if="interfaceDocData.data.requestParamsList?.length > 0"
            >
              <el-table-column
                :prop="it.prop"
                :label="it.lable"
                :formatter="it.formmator"
                v-for="it in interfaceDocColumns"
                align="center"
              />
            </el-table>
            <div v-else style="text-align: center">
              <el-icon><RemoveFilled /></el-icon>
              无参数
            </div>
          </el-collapse-item>
          <el-collapse-item title="请求参数（Body）" name="3">
            <el-table
              :data="interfaceDocData.data.requestBodyList"
              stripe
              style="width: 100%"
              v-if="interfaceDocData.data.requestBodyList?.length > 0"
            >
              <el-table-column
                :prop="it.prop"
                :label="it.lable"
                :formatter="it.formmator"
                v-for="it in interfaceDocColumns"
                align="center"
              />
            </el-table>
            <div v-else style="text-align: center">
              <el-icon><RemoveFilled /></el-icon>
              无参数
            </div>
          </el-collapse-item>
          <el-collapse-item title="代码示例" name="4">
            <prism-editor
              class="my-editor"
              v-model="interfaceDocData.data.codeDemo"
              aria-disabled
              :highlight="highlighter"
              line-numbers
              :readonly="true"
              :tabSize="4"
            ></prism-editor>
          </el-collapse-item>
          <el-collapse-item title="返回示例" name="5">
            <json-viewer
              :value="interfaceDocData.data.responseData"
              style="display: inline-block"
            ></json-viewer>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>
    <div v-else-if="activeMenuIndex === '2'">
      <div class="interface_invoke public_own">
        <div style="margin-bottom: 20px" v-if="interfaceDocData.data.requestHeaderList?.length > 0">
          <div class="header_lable">请求参数（Headers）</div>
          <div v-for="it in requestHeaders">
            <span style="color: red" v-if="it.required">*</span>
            <el-input
              v-model="it.name"
              style="width: 300px; margin: 10px 20px"
              :disabled="it.required"
            />
            <span>=</span>
            <el-input v-model="it.value" style="width: 300px; margin: 10px 20px" @blur="validateHeaderParams"></el-input>
          </div>
        </div>
        <div style="margin-bottom: 20px" v-if="interfaceDocData.data.requestParamsList?.length > 0">
          <div class="header_lable">请求参数（Params）</div>
          <div v-for="it in requestParams">
            <span style="color: red" v-if="it.required">*</span>
            <el-input
              v-model="it.name"
              style="width: 300px; margin: 10px 20px"
              :disabled="it.required"
            />
            <span>=</span>
            <el-input v-model="it.value" style="width: 300px; margin: 10px 20px"></el-input>
          </div>
        </div>
        <div style="margin-bottom: 20px" v-if="interfaceDocData.data.requestBodyList?.length > 0">
          <div class="header_lable">请求参数（Body）</div>
          <div v-for="it in requestBody">
            <span style="color: red" v-if="it.required">*</span>
            <el-input
              v-model="it.name"
              style="width: 300px; margin: 10px 20px"
              :disabled="it.required"
            />
            <span>=</span>
            <el-input v-model="it.value" style="width: 300px; margin: 10px 20px"></el-input>
          </div>
        </div>
        <el-button type="primary" style="margin: 10px 0 0 300px" @click="interfaceInvoke">
          在线调试
        </el-button>
      </div>
      <div class="invoke_result public_own">
        <div class="header_lable">返回结果</div>
        <json-viewer :value="responseParams.data"></json-viewer>
      </div>
    </div>
    <div v-else class="comment">
      <div class="comment_box">
        <el-button type="primary" plain @click="addComment">添加评论</el-button>
        <el-divider />
      </div>
      <div class="comment_box" v-for="it in commentList.list">
        <div class="comment_header">
          <div>
            <span>评分：</span>
            <span class="comment_score">{{ it.score }}分</span>
          </div>
          <span class="comment_createDate">{{ it.createDate }}</span>
          <span>{{ it.username }}</span>
        </div>
        <div class="comment_content">{{ it.comment }}</div>
        <el-divider />
      </div>
      <Pagination
        :current="commentPagination.current"
        :total="commentPagination.total"
        @changePage="handleChangeCommentCurrent"
      ></Pagination>
    </div>
    <el-dialog
      v-model="commentDialogVisible"
      title="添加评论"
      width="30%"
      :before-close="handleCommentDialogClose"
    >
      <el-form :model="commentForm" ref="commentFormRef" :rules="commentRule">
        <el-form-item label="评分" :label-width="formLabelWidth" prop="score">
          <el-rate v-model="commentForm.score" :colors="commentColors" />
        </el-form-item>
        <el-form-item label="评论" :label-width="formLabelWidth" prop="comment">
          <el-input v-model="commentForm.comment" autocomplete="off" placeholder="请输入评论" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="commentDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddComment">发布</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import Pagination from "@/components/Pagination/index.vue"
import {
  getInterfaceDetail,
  getComments,
  interfaceComments,
  interfaceDoc,
  genAPISign,
} from "@/api/home"
import request from "@/utils/request.js"
import JsonViewer from "vue-json-viewer"
import { PrismEditor } from "vue-prism-editor"
import "vue-prism-editor/dist/prismeditor.min.css" // import the styles somewhere
// import highlighting library (you can use any library you want just return html string)
import { highlight, languages } from "prismjs/components/prism-core"
import "prismjs/components/prism-clike"
import "prismjs/components/prism-javascript"
import "prismjs/themes/prism-tomorrow.css" // import syntax highlighting styles
import useUserStore from "@/store/modules/user.js"
import { v4 as uuidv4 } from "uuid"
import { getToken } from "@/utils/auth"
import { ElMessage, ElMessageBox } from "element-plus"
import { isRelogin } from "@/utils/request"
const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
let interfaceId = ref("")
onMounted(() => {
  interfaceId = route.query.id //查询的interfaceId
  if (interfaceId) {
    handleGetInterfaceDetail(interfaceId)
    getInterfaceDoc()
  }
})
//接口信息
const dataColumns = reactive([
  {
    label: "接口名称",
    value: "lalalalalla",
  },
  {
    label: "描述",
    value: "百度热点数据",
  },
  {
    label: "请求地址",
    value: "/api/baidu/baiduInfo",
    canCopy: true,
  },
  {
    label: "请求方法",
    value: "POST",
    state: true,
  },
  {
    label: "创建时间",
    value: "2022-11-28 17:41:15",
  },
  {
    label: "更新时间",
    value: "2023-06-07 10:34:23",
  },

  {
    label: "请求头",
    value: '{ "Content-Type": "application/json" }',
    canCopy: true,
  },
  {
    label: "响应头",
    value: '{ "Content-Type": "application/json" }',
    canCopy: true,
  },
  {
    label: "接口状态",
    value: "正常",
    state: true,
  },
  {
    label: "价格",
    value: "免费",
    state: true,
  },
  {
    label: "请求参数",
    value: { name: "lalal", age: 55 },
    isJson: true,
    canCopy: true,
  },
])
let interfaceDetail = reactive({
  detail: {
    packages: [],
  },
  selectPackage: {
    packageId: "",
  },
})
let purchaseNum = ref(1)
async function handleGetInterfaceDetail(id) {
  let { data } = await getInterfaceDetail(id)
  interfaceDetail.detail = data
  interfaceDetail.selectPackage = data.packages[0]
}
function handleChangePackage(idx) {
  interfaceDetail.selectPackage = interfaceDetail.detail.packages[idx]
}
function goOrderPay() {
  router.push({
    path: "/order/pay",
    query: {
      interfacePackageId: interfaceDetail.selectPackage.packageId,
      count: purchaseNum.value,
    },
  })
}
//中部菜单
let activeMenuIndex = ref("1")
function handleMenuSelect(idx) {
  if (idx == "2" && !getToken()) {
    ElMessageBox.confirm("还未登录，请先登录", "系统提示", {
      confirmButtonText: "登录",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      isRelogin.show = false
      useUserStore().cleanData()
      router.push(`/login?redirect=${route.path}&query=${JSON.stringify(route.query)}`)
    })
    return
  }
  activeMenuIndex.value = idx
}
//api文档
//ref(["1","2","3","4","5"])
let collapseActiveIndex = computed(() => {
  let arr = ["4", "5"]
  if (interfaceDocData.data?.requestHeaderList?.length > 0) {
    arr.push('1')
  }
  if (interfaceDocData.data?.requestParamsList?.length > 0) {
    arr.push('2')
  }
  if (interfaceDocData.data?.requestBodyList?.length > 0) {
    arr.push('3')
  }
  return arr
})
function handleCollapseChange() {}
const interfaceDocColumns = [
  {
    prop: "name",
    lable: "名称",
  },
  {
    prop: "type",
    lable: "类型",
  },
  {
    prop: "required",
    lable: "是否必须",
    formmator: (val) => (val.required ? "是" : "否"),
  },
  {
    prop: "description",
    lable: "描述",
  },
]
const interfaceDocData = reactive({
  data: {},
})
async function getInterfaceDoc() {
  let { data } = await interfaceDoc(interfaceId)
  interfaceDocData.data = data
}
function highlighter(code) {
  return highlight(code, languages.js) // returns html
}
//在线调试
let requestHeaders = ref([])
let requestParams = ref([])
let requestBody = ref([])
let responseParams = reactive({
  data: null,
})
function handleParams(data) {
  console.log(data)
  if (!data) return []
  return data.map((it) => ({ ...it, value: "" }))
}
function handleObject(arr) {
  console.log(arr)
  let obj = {}
  if (arr == []) return obj
  arr.forEach((element) => {
    obj[element.name] = element.value
  })
  return obj
}
function validateHeaderParams(e) {
  console.log(e.target.value);
  if (!IsChar(e.target.value)) {
    ElMessage({ type: 'error', message:'请求头参数不能包含中文！'})
  }
}
function IsChar(s)
{ 
 var Number = "0123456789.abcdefghijklmnopqrstuvwxyz-\/ABCDEFGHIJKLMNOPQRSTUVWXYZ`~!@#$%^&*()_";
 for (let i = 0; i < s.length;i++)
    {   
        var c = s.charAt(i);
        if (Number.indexOf(c) == -1) return false;
    }
 return true
}
async function interfaceInvoke() {
  let requestHeaderParams = handleObject(toRaw(requestHeaders.value))
  if (JSON.stringify(requestHeaderParams) !='{}') {
    let hasHanZi = Object.values(requestHeaderParams).every(it => IsChar(it))
    if (!hasHanZi) {
    ElMessage({ type: 'error', message: '请求头参数不能包含中文！' })
    return
  }
  }
  const nonce = uuidv4()
  const timestamp = Math.floor(new Date() / 1000)
  let { data: sign } = await genAPISign({ nonce, timestamp })
  let headers = {
    accessKey: userStore.userData.accessKey,
    nonce,
    timestamp,
    sign,
    token: getToken(),
    url: interfaceDocData.data.url,
    ...requestHeaderParams,
  }
  console.log("headers", headers)
  let method = interfaceDocData.data.method
  let params = handleObject(toRaw(requestParams.value))
  let data = handleObject(toRaw(requestBody.value))
  let axiosConfig = {}
  if (method == "GET") {
    axiosConfig = {
      method,
      params,
      headers,
      timeout: 6000,
    }
  } else {
    axiosConfig = {
      method,
      params,
      data,
      headers,
      timeout: 6000,
    }
  }

  request(axiosConfig).then(
    (res) => {
      responseParams.data = res
    },
    (err) => {
      console.log(err)
      ElMessage({
        type: "error",
        message: err.response ? err.response.data?.msg : err.message,
      })
    },
  )
}
//评论
let commentPagination = reactive({
  current: "1",
  total: "1",
})
let commentList = reactive({
  list: [],
})
let commentDialogVisible = ref(false)
let commentFormRef = ref()
let commentForm = ref({
  score: null,
  comment: "",
})
const commentRule = {
  score: [{ required: true, trigger: "blur", message: "请评分" }],
  comment: [{ required: true, trigger: "blur", message: "请输入评论" }],
}
let commentColors = ref(["#99A9BF", "#F7BA2A", "#FF9900"])
async function handleGetComment() {
  let { data } = await getComments({
    current: commentPagination.current,
    interfaceInfoId: interfaceId,
    limit: 10,
  })
  commentList.list = data.list
  commentPagination.total = Math.ceil(data.total / 10)
}
function handleChangeCommentCurrent(page) {
  commentPagination.current = page
  handleGetComment()
}
function resetForm(formEl) {
  if (!formEl) return
  formEl.resetFields()
}
function addComment() {
  commentDialogVisible.value = true
  resetForm(commentFormRef.value)
}
function handleAddComment() {
  if (!commentFormRef) return
  commentFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let { data } = await interfaceComments({
          interfaceInfoId: interfaceId,
          ...commentForm.value,
        })
        if (data) {
          ElMessage({ type: "success", message: "评论成功" })
          commentDialogVisible.value = false
          commentPagination.current = "1"
          handleGetComment()
          return
        }
      } catch (error) {
        commentDialogVisible.value = false
      }
    } else {
      return false
    }
  })
}
function handleCommentDialogClose(done) {
  done()
}
watch(activeMenuIndex, () => {
  if (activeMenuIndex.value === "3") {
    handleGetComment()
  } else if (activeMenuIndex.value === "2") {
    nextTick(() => {
      requestHeaders.value = handleParams(toRaw(interfaceDocData.data.requestHeaderList))
      requestParams.value = handleParams(toRaw(interfaceDocData.data.requestParamsList))
      requestBody.value = handleParams(toRaw(interfaceDocData.data.requestBodyList))
    })
  }
})
</script>

<style lang="scss" scoped>
.interface-info {
  .header-title {
    font-size: 1.5rem;
    display: block;
    text-indent: 1.5rem;
  }
  .public_own {
    margin: 15px;
    border: 1px solid #ccc;
    padding: 20px;
    border-radius: 10px;
    .header_lable {
      font-weight: 700;
      margin-bottom: 14px;
    }
  }
  .interface_invoke {
  }
}
.interfaceDoc {
  .interfaceDoc_header {
    font-weight: 700;
  }
  .header_item {
    margin: 14px;
    .item_content {
      // font-size: 18px;
    }
    .invokeUrl {
      color: chocolate;
    }
  }
  .demo-collapse {
    margin: 20px 10px;
  }
}
.comment {
  padding-top: 20px;
  .comment_box {
    padding: 15px 60px;
    .comment_header {
      display: flex;
      justify-content: space-between;
      margin-bottom: 20px;
      .comment_score {
        color: coral;
      }
      .comment_createDate {
        color: #aaa;
      }
    }
    .comment_content {
      font-weight: 700;
    }
  }
}
.my-editor {
  background: #2d2d2d;
  border-radius: 10px;
  color: #ccc;
  font-family: Fira code, Fira Mono, Consolas, Menlo, Courier, monospace;
  font-size: 14px;
  line-height: 1.5;
  padding: 5px;
  margin-top: 10px;
}
.my-editor-title {
  color: white;
  margin-top: 10px;
}
.prism-editor__textarea:focus {
  outline: none;
}
</style>
