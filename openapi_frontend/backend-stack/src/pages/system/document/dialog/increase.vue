<template>
  <el-dialog custom-class="on-increase" v-model="dialog.visible" fullscreen modal="false" align-center destroy-on-close>
    <template #header>
      <b>添加文档</b>
      <span>(需要手动保存，才会保存数据，提交才会生效)</span>
    </template>
    <mavon-editor
      ref="md"
      v-model="docs.ctx"
      :imageFilter="acceptImgType"
      :subfield="false"
      :defaultOpen="'editor'"
      @save="saveDocs"
      @imgAdd="addImages"
      @imgDel="delImages"
      navigation
    />
    <template #footer>
      <el-button type="primary" @click="submitDocs">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { wrap, acceptImgType } from "./common"
import { createSystemDocument } from "@/api/system/document"
import { getUploadImgUrl } from "@/api/file"
import { base64ToBlob } from "@/utils/file"
import { ElMessage } from "element-plus"

const { proxy } = getCurrentInstance()

const dialog = reactive({
  visible: false,
})
const docs = reactive({
  title: "",
  content: "",
  ctx: "",
})
const file = ref(new FormData())
const imgUrlList = reactive([])

function openDialog() {
  dialog.visible = true
}

function saveDocs() {
  // 保存文档处理
  const data = docs.ctx.split("\n")
  docs.title = data[0]
  docs.content = ""
  if (data.length > 1) {
    const content = data.slice(1, data.length)
    docs.content = content.join(wrap)
  }
}

function addImages(pos, File) {
  const sortNumber = pos + "."
  const blobFile = base64ToBlob(File.miniurl)
  const fileName = File.name

  let imgUrl

  file.value.append("file", blobFile, sortNumber + fileName)
  getUploadImgUrl(file.value).then((res) => {
    if (res.code === 0) {
      imgUrl = res.data
      // 替换原来的图片地址
      proxy.$refs.md.$img2Url(pos, imgUrl)
      imgUrlList[pos - 1] = imgUrl
    }
  })
  file.value = new FormData()
}

function delImages([pos, File]) {
  imgUrlList[pos - 1] = undefined
}

function submitDocs() {
  const { title, content } = docs
  createSystemDocument({ title: title, content: content }).then((res) => {
    if (res.code === 0) {
      ElMessage({
        message: "添加文档成功",
        type: "success",
      })
      dialog.visible = false
    }
  })
}

defineExpose({
  openDialog,
})
</script>

<style lang="scss" scoped>
.on-increase {
  .v-note-wrapper {
    min-height: calc(100vh - 155px);
  }
  > footer.el-dialog__footer {
    text-align: left;
    padding: 0 20px;
  }
}
</style>
