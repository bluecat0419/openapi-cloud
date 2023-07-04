<template>
  <el-dialog
    class="upload-dialog"
    v-model="dialogState"
    :modal="false"
    title="上传头像"
    width="40%"
    @open="toggleDialog(true)"
    @close="toggleDialog(false)"
    center
    alignCenter
    :destroy-on-close="true"
  >
    <el-row class="upload-avatar" justify="space-evenly" align="middle">
      <el-col :span="8">
        <el-upload
          class="upload"
          list-type="picture-card"
          accept="image/*"
          :auto-upload="true"
          :limit="1"
          :http-request="myUploadFile"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">上传头像</div>
          <template #file="{ file }">
            <img class="el-upload-list__item-thumbnail" :src="file.url" />
            <span class="el-upload-list__item-actions">
              <span class="el-upload-list__item-delete" @click="cleanAvatar(file)">
                <el-icon><Delete /></el-icon>
              </span>
            </span>
          </template>
        </el-upload>
      </el-col>
      <el-col :span="1">
        <el-divider direction="vertical" />
      </el-col>
      <el-col :span="8">
        <div class="avatar-logo">
          <img
            :src="imgUrl ? avatarMap.get('preview') : avatarMap.get('current')"
            alt="avatar"
            width="80"
            height="80"
          />
          <span>{{ imgUrl ? "预览头像" : "当前头像" }}</span>
        </div>
      </el-col>
    </el-row>

    <div class="upload-tip">请选择图片上传：大小180 * 180像素支持JPG、PNG等格式，图片需小于2M</div>

    <template #footer>
      <span class="dialog-footer">
        <template v-if="!imgUrl">
          <el-button type="primary" disabled>更新</el-button>
          <el-button @click="toggleDialog(false)">取消</el-button>
        </template>
        <template v-else>
          <el-button type="primary" @click="updateAvatar">更新</el-button>
          <el-button @click="cleanAvatar">返回</el-button>
        </template>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { getUploadUrl } from "@/api/user/profile"

const props = defineProps({
  state: {
    type: Boolean,
    default: false,
  },
  data: {
    type: Object,
    required: true,
  },
})
const emit = defineEmits(["updateState", "update"])

const dialogState = computed(() => props.state)
const key = Object.keys(props.data)[0]
const imgUrl = ref()
const avatarMap = computed(() => {
  return new Map([
    ["current", props.data[key]],
    ["preview", imgUrl.value],
  ])
})

function toggleDialog(state) {
  if (typeof state === "boolean") {
    emit("updateState", state)
  } else if (state === undefined) {
    emit("updateState", !dialogState.value)
  }
}

function myUploadFile(data) {
  const fileInfo = data.file
  const type = fileInfo.type.split("/")
  const fileName = fileInfo.name
  let formData = new FormData()
  formData.append("file", fileInfo, fileName)
  if (type[0] === "image") {
    getUploadUrl(formData).then((res) => {
      console.log(res)
      imgUrl.value = res.data
    })
  }
}

function cleanAvatar(file) {
  if (file) {
    console.log(file)
  }
  imgUrl.value = ""
}

function updateAvatar() {
  const updateData = ref({})
  updateData.value[key] = imgUrl
  emit("update", updateData.value)
  toggleDialog(false)
}
</script>

<style lang="scss" scoped>
.upload-avatar {
  height: 150px;
  overflow: hidden;

  .upload {
    display: inline-block;
  }

  .el-col {
    height: 100%;
  }

  .el-col-1 {
    height: 80%;
    .el-divider--vertical {
      height: 100%;
    }
  }

  .avatar-logo {
    display: flex;
    flex-flow: column nowrap;
    justify-content: space-evenly;
    align-items: center;
    height: 100%;

    > img {
      border-radius: 50%;
    }
  }
}

.upload-tip {
  text-align: center;
  font-size: 0.75rem;
  margin-top: 10px;
}

.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>
