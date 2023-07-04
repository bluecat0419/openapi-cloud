<template>
  <div class="user-profile">
    <header>
      <svg-icon v-if="route.meta && route.meta.icon" :icon-class="route.meta.icon" />
      <span>个人中心</span>
    </header>

    <el-row class="user-info adjacent-edge-projection" align="middle">
      <el-col style="text-align: center" :span="4" class="avatar">
        <img class="avatar-logo" :src="userInfo.avaUrl" alt="头像" width="80" height="80" />
        <a class="avatar-cover" href="javascript:" @click="toggleDialog(true)">更换头像</a>
        <span style="display: block">头像</span>
      </el-col>
      <el-col :span="20">
        <el-descriptions size="large" :column="4">
          <el-descriptions-item label="用户名称">
            <span>{{ userInfo.username }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="真实姓名">
            <ModifyInput :data="{ realName: userInfo.realName }" @update="updateUserInfo" />
          </el-descriptions-item>
          <el-descriptions-item label="手机号">
            <ModifyInput :data="{ mobile: userInfo.mobile }" @update="updateUserInfo" />
          </el-descriptions-item>
          <el-descriptions-item label="邮箱">
            <ModifyInput :data="{ email: userInfo.email }" @update="updateUserInfo" />
          </el-descriptions-item>
          <el-descriptions-item label="性别">
            <ModifyInput
              :data="{ gender: userInfo.gender }"
              type="select"
              :options="genderOption"
              @update="updateUserInfo"
            />
          </el-descriptions-item>
          <el-descriptions-item label="修改密码">
            <el-tag type="warning" @click="handleChangePassword" style="cursor: pointer">
              修改
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="accessKey">
            <div v-if="userInfo.accessKey">
              <span>{{ userInfo.accessKey }}</span>
              <el-tag
                type="warning"
                @click="handleOpenAskDialog('ask')"
                style="cursor: pointer; margin-left: 6px"
              >
                更新
              </el-tag>
            </div>
            <el-tag
              type="warning"
              @click="handleOpenAskDialog('ask')"
              style="cursor: pointer"
              v-else
            >
              创建
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="secretKey">
            <el-tag
              type="warning"
              style="cursor: pointer"
              v-if="userInfo.accessKey"
              @click="handleOpenAskDialog('ssk')"
            >
              查看
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
    <div class="user-info adjacent-edge-projection" style="margin-top: 30px">
      <div>我的接口</div>
      <div style="margin-top: 30px">
        <el-table :data="interfaceTableData.tableData" stripe>
          <el-table-column
            :prop="it.prop"
            :label="it.label"
            :formatter="it.formator"
            v-for="it in tableColumns"
            :key="it.prop"
            :width="it.width"
            align="center"
          >
            <template #default="scope" v-if="it.template">
              <div style="display: flex; align-items: center">
                <el-button type="warning" plain @click="goInterfaceInfo(scope.row.interfaceInfoId)">
                  查看文档
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <Pagination
          :current="paginationData.current"
          :total="paginationData.total"
          @changePage="handleChangePage"
        ></Pagination>
      </div>
    </div>
    <upload-dialog
      :data="{ avaUrl: userInfo.avaUrl }"
      v-model:state="dialogState"
      @updateState="toggleDialog"
      @update="updateUserInfo"
    />
    <el-dialog v-model="pwdDialogVisible" title="修改密码" width="30%">
      <el-form label-position="top" ref="ruleFormRef" :model="ruleForm" :rules="rules">
        <el-form-item label="旧密码" prop="oldPass">
          <el-input v-model="ruleForm.oldPass" type="password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="pass">
          <el-input v-model="ruleForm.pass" type="password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input v-model="ruleForm.checkPass" type="password" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="pwdDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm(ruleFormRef)">提交</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      v-model="askDialogVisible"
      :title="isAsk ? 'accessKey' : 'secretKey'"
      width="30%"
      :before-close="handleAskDialogClose"
    >
      <el-menu
        :default-active="activeAskIndex"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleAskSelect"
      >
        <el-menu-item index="1">密码验证</el-menu-item>
        <el-menu-item index="2" :disabled="!userInfo.mobile">手机号验证</el-menu-item>
        <el-menu-item index="3" :disabled="!userInfo.email">邮箱验证</el-menu-item>
      </el-menu>
      <el-form v-if="activeAskIndex == '1'" :model="pwdForm" ref="pwdFormRef" class="app-container">
        <el-form-item label="账号">
          <span>{{ userInfo.username }}</span>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input placeholder="请输入密码" v-model="pwdForm.password" type="password"></el-input>
        </el-form-item>
      </el-form>
      <el-form
        v-else-if="activeAskIndex == '2'"
        :model="phoForm"
        ref="phoFormRef"
        class="app-container"
      >
        <el-form-item label="手机号码">
          <span>{{ userInfo.mobile }}</span>
        </el-form-item>
        <el-form-item label="验证码" prop="phoCode">
          <el-input
            placeholder="请输入短信验证码"
            v-model="phoForm.phoCode"
            style="width: 140px; margin-right: 10px"
          ></el-input>
          <div class="login-code">
            <el-button v-if="!isAskPhoVisible" type="primary" @click="sendPhoCode()">
              发送短信验证码
            </el-button>
            <el-button v-else type="primary" disabled>{{ isAskPhoVisible }}</el-button>
          </div>
        </el-form-item>
      </el-form>
      <el-form v-else :model="emaForm" ref="emaFormRef" class="app-container">
        <el-form-item label="邮箱">
          <span>{{ userInfo.email }}</span>
        </el-form-item>
        <el-form-item label="验证码" prop="emaCode">
          <el-input
            placeholder="请输入邮箱验证码"
            v-model="emaForm.emaCode"
            style="width: 140px; margin-right: 10px"
          ></el-input>
          <div class="login-code">
            <el-button v-if="!isAskEmaVisible" type="primary" @click="sendEmaCode()">
              发送邮箱验证码
            </el-button>
            <el-button v-else type="primary" disabled>{{ isAskEmaVisible }}</el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="askDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAskForm">确认</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      v-model="sskDialogVisible"
      :before-close="handleSskDialogClose"
      width="28%"
      :title="isAsk ? 'accessKey & secretKey' : 'secretKey'"
    >
      <el-form label-position="top">
        <el-form-item label="accessKey" v-if="isAsk">
          <div>
            {{ accessKey }}
            <CopyIcon :value="accessKey"></CopyIcon>
          </div>
        </el-form-item>
        <el-form-item label="secretKey">
          <div>
            {{ secretKey }}
            <CopyIcon :value="secretKey"></CopyIcon>
          </div>
        </el-form-item>
      </el-form>
      <div style="margin-top: 20px; color: #aaa; text-align: center">
        请妥善保存secretKey，不要泄漏
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import ModifyInput from "./modifyInput.vue"
import UploadDialog from "./uploadDialog.vue"
import useUserStore from "@/store/modules/user"
import CopyIcon from "@/components/CopyIcon/index.vue"
import Pagination from "@/components/Pagination/index.vue"
import { reciprocalTime } from "@/utils/countdown"
import {
  changePassword,
  genAccessKey,
  getSecretKey,
  sendAccessKeyEmailCode,
  sendAccessKeySmsCode,
  myInterfacePage,
} from "@/api/user/profile.js"
import { ElMessage, ElMessageBox } from "element-plus"
import { Star } from "@element-plus/icons-vue"
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
//用户信息
const dialogState = ref(false)
const genderOption = ref(
  new Map([
    [0, "男"],
    [1, "女"],
    [2, "保密"],
  ]),
)
const userInfo = computed(() => {
  return {
    avaUrl: userStore.userData.avaUrl,
    username: userStore.userData.username,
    realName: userStore.userData.realName,
    mobile: userStore.userData.mobile,
    email: userStore.userData.email,
    gender: userStore.userData.gender,
    accessKey: userStore.userData.accessKey,
  }
})

function toggleDialog(state) {
  if (typeof state === "boolean") {
    dialogState.value = state
  } else if (state === undefined) {
    dialogState.value = !dialogState.value
  }
}

function updateUserInfo(obj) {
  const copyUserInfo = Object.assign({}, userInfo.value)
  delete copyUserInfo.username
  if (Object.keys(obj).length === 1 && Object.keys(obj)[0] in userInfo.value) {
    const key = Object.keys(obj)[0]
    delete copyUserInfo[key]
    const submitData = Object.assign({}, copyUserInfo, obj)
    userStore.updateUserData(submitData).then(() => {
      console.log("修改用户数据成功！")
    })
  }
}
//密码
let pwdDialogVisible = ref(false)
const ruleFormRef = ref()
const ruleForm = reactive({
  oldPass: "",
  pass: "",
  checkPass: "",
})
const rules = reactive({
  oldPass: [{ validator: validateOldPass, trigger: "blur" }],
  pass: [{ validator: validatePass, trigger: "blur" }],
  checkPass: [{ validator: validatePass2, trigger: "blur" }],
})
function validateOldPass(rule, value, callback) {
  if (value === "") {
    callback(new Error("请输入旧密码"))
  } else {
    callback()
  }
}
function validatePass(rule, value, callback) {
  if (value === "") {
    callback(new Error("请输入新密码"))
  } else {
    if (ruleForm.checkPass !== "") {
      if (!ruleFormRef.value) return
      ruleFormRef.value.validateField("checkPass", () => null)
    }
    callback()
  }
}
function validatePass2(rule, value, callback) {
  if (value === "") {
    callback(new Error("请输入确认密码"))
  } else if (value !== ruleForm.pass) {
    callback(new Error("两次密码不一致!"))
  } else {
    callback()
  }
}
function handleChangePassword() {
  pwdDialogVisible.value = true
  resetForm(ruleFormRef.value)
}
const submitForm = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      let result = await changePassword({
        newPassword: ruleForm.pass,
        oldPassword: ruleForm.oldPass,
      })
      userStore.cleanData()
      ElMessage({
        message: "修改成功，请重新登录",
        type: "success",
        duration: 800,
      })
      router.push("/login")
      console.log(result)
    } else {
      console.log("error submit!")
      return false
    }
  })
  pwdDialogVisible.value = false
}
const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
}
//accessKey && secretKey
let askDialogVisible = ref(false)
let sskDialogVisible = ref(false)
let isAsk = ref(true)
let activeAskIndex = ref("1")
const pwdFormRef = ref()
const phoFormRef = ref()
const emaFormRef = ref()
let pwdForm = reactive({
  password: "",
})
let phoForm = reactive({
  phoCode: "",
})
let emaForm = reactive({
  emaCode: "",
})
// 当前短信验证码计时
const askTimingPho = ref(false)
const sckTimingPho = ref(false)
// 当前邮箱验证码计时
const askTimingEma = ref(false)
const sckTimingEma = ref(false)
const secretKey = ref("")
const accessKey = ref("")
let isAskPhoVisible = computed(() => isAsk.value ? askTimingPho.value : sckTimingPho.value)
let isAskEmaVisible=computed(()=> isAsk.value ? askTimingEma.value : sckTimingEma.value)
function handleOpenAskDialog(type) {
  isAsk.value = type == "ask" ? true : false
  activeAskIndex.value = "1"
  askDialogVisible.value = true
}
function sendPhoCode() {
  sendAccessKeySmsCode(isAsk.value ? "0" : "1")
    .then((res) => {
      ElMessage({
        message: "发送成功",
        type: "success",
      })
    })
    .then(() => {
      if (isAsk.value) {
        const { start } = reciprocalTime(60, (value) => {
        start()
        if (parseInt(value) !== 0) {
            askTimingPho.value = value
          
        } else {
            askTimingPho.value = false
        }
      })
      } else {
        const { start } = reciprocalTime(60, (value) => {
        start()
        if (parseInt(value) !== 0) {
            sckTimingPho.value = value
          
        } else {
            sckTimingPho.value = false
          
        }
      })
      }
    })
}
function sendEmaCode() {
  sendAccessKeyEmailCode(isAsk.value ? "0" : "1")
    .then((res) => {
      ElMessage({
        message: "发送成功",
        type: "success",
      })
    })
    .then(() => {
      if (isAsk.value) {
        const { start } = reciprocalTime(60, (value) => {
        start()
        if (parseInt(value) !== 0) {
            askTimingEma.value = value
        } else {
            askTimingEma.value = false
        }
      })
      } else {
        const { start } = reciprocalTime(60, (value) => {
        start()
        if (parseInt(value) !== 0) {
            sckTimingEma.value = value
        } else {
            sckTimingEma.value = false
        }
      })
      }
      
    })
}
async function submitAskForm() {
  let params =
    activeAskIndex.value == "1"
      ? { checkType: 0, code: "", password: pwdForm.password }
      : activeAskIndex.value == "2"
      ? { checkType: 1, code: phoForm.phoCode, password: "" }
      : { checkType: 2, code: emaForm.emaCode, password: "" }
  let { data } = isAsk.value ? await genAccessKey(params) : await getSecretKey(params)
  console.log("data", data)
  if (data) {
    if (isAsk.value) {
      userStore.updateAccessKey(data.accessKey).then(() => {
        ElMessage({ type: "success", message: "AccessKey创建成功" })
        accessKey.value = data.accessKey
        secretKey.value = data.secretKey
        sskDialogVisible.value = true
      })
    } else {
      secretKey.value = data
      sskDialogVisible.value = true
      console.log(44444)
    }
  }
}
function handleAskSelect(idx) {
  activeAskIndex.value = idx
}
function handleAskDialogClose(done) {
  pwdForm.password = ""
  phoForm.phoCode = ""
  emaForm.emaCode = ""
  done()
}
function handleSskDialogClose(done) {
  askDialogVisible.value = false
  pwdForm.password = ""
  phoForm.phoCode = ""
  emaForm.emaCode = ""
  done()
}
//我的接口列表
let interfaceTableData = reactive({
  tableData: [],
})
const tableColumns = [
  {
    prop: "interfaceName",
    label: "接口名称",
  },
  {
    prop: "status",
    label: "状态",
    formator: (v) => (v.status == 1 ? "可用" : "不可用"),
  },
  {
    prop: "leftNum",
    label: "剩余次数",
  },
  {
    prop: "totalNum",
    label: "总次数",
  },
  {
    prop: "createDate",
    label: "购买时间",
  },
  {
    template: true,
  },
]
const paginationData = reactive({
  current: "1",
  total: "1",
})
async function getInterfaceList() {
  let { data } = await myInterfacePage({ current: paginationData.current, limit: 10 })
  interfaceTableData.tableData = data.list
}
const handleChangePage = function (page) {
  paginationData.current = page
  handleHomePage()
}
function goInterfaceInfo(id) {
  router.push({ path: "/interface/info", query: { id } })
}
onMounted(() => {
  getInterfaceList()
})
</script>

<style lang="scss" scoped>
.user-profile {
  margin: 5px 40px;
  min-height: calc(100vh - 50px);

  .user-info {
    padding: 20px;

    .avatar {
      position: relative;

      img.avatar-logo {
        border-radius: 50%;
      }

      > a.avatar-cover {
        position: absolute;
        top: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 80px;
        height: 80px;
        color: #fff;
        line-height: 80px;
        font-size: 0.8rem;
        border-radius: 50%;
        background-color: rgba(0, 0, 0, 0.6);
        opacity: 0;
        transition: opacity 0.5s;
        &:hover {
          opacity: 1;
        }
      }
    }

    :deep(.el-descriptions__cell) {
      width: 25%;
    }
  }
}
</style>
