<template>
  <div class="interface-icon clearfix">
    <el-upload
      v-if="!iconUrl"
      class="interface-upload"
      v-model="file"
      list-type="text"
      accept="image/*"
      :show-file-list="false"
      :auto-upload="true"
      :limit="1"
      :http-request="handleUploadIcon"
    >
      <el-button type="plain">点击上传</el-button>
      <template #tip>
        <div class="el-upload__tip">jpg/png files with a size less than 500KB.</div>
      </template>
    </el-upload>
    <div v-else class="interface-img">
      <img :src="iconUrl" @click="clearIcon" />
      <span class="img-options">
        <el-icon @click="clearIcon" title="清除图标"><DeleteFilled /></el-icon>
      </span>
    </div>
  </div>
</template>

<script setup>
import { getUploadImgUrl } from "@/api/file.js"
import { ElMessage } from "element-plus"

const props = defineProps({
  icon: {
    type: String,
    required: true,
  },
})
const emit = defineEmits([])
const file = ref(new FormData())
const iconUrl = computed(() => props.icon)

function fileInit() {
  file.value = new FormData()
}

function clearIcon() {
  emit("update:icon", "")
}

function handleUploadIcon(data) {
  const fileInfo = data.file
  const fileType = fileInfo.type.split("/")[0]
  const fileName = fileInfo.name
  file.value.append("file", fileInfo, fileName)
  if (fileType === "image") {
    getUploadImgUrl(file.value).then((res) => {
      if (res.code === 0) {
        emit("update:icon", res.data)
      }
    })
    return
  }
  ElMessage.warning("上传格式有误！")
  fileInit()
}
</script>

<style lang="scss" scoped>
.interface-icon {
  height: 50px;
  :deep(.el-upload--text) {
    float: left;
  }

  .el-upload__tip {
    margin: 0;
    margin-left: 150px;
    width: 100%;
    text-align: center;
    color: goldenrod;
  }

  .interface-img {
    position: relative;
    width: 50px;
    height: 50px;
    > img {
      width: 50px;
      height: 50px;
      border-radius: 50%;
      outline: 1px solid gray;
      object-fit: cover;
    }

    > span.img-options {
      position: absolute;
      font-size: 1.5rem;
      line-height: 50px;
      text-align: center;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      opacity: 0;

      .el-icon {
        display: none;
        cursor: pointer;
      }
    }

    &:hover {
      > img {
        opacity: 0.2;
      }

      > span.img-options {
        opacity: 1;

        .el-icon {
          display: inline-flex;
        }
      }
    }
  }
}
</style>
