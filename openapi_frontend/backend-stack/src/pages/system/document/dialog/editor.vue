<template>
  <el-dialog custom-class="on-editor" v-model="dialog.visible" fullscreen modal="false" align-center destroy-on-close>
    <template #header>
      <b>编辑文档</b>
      <span>(需要手动保存)</span>
    </template>
    <mavon-editor ref="md" v-model="docs.ctx" :imageFilter="acceptImgType" @save="saveDocs" @imgAdd="addImages" />
  </el-dialog>
</template>

<script setup>
import { wrap, acceptImgType } from "./common"
import { getUploadImgUrl } from "@/api/file"
import { base64ToBlob } from "@/utils/file"

const props = defineProps({
  ctx: {
    type: Object,
    default: { id: "", title: "", content: "" },
  },
})
const emits = defineEmits()
const { proxy } = getCurrentInstance()

const dialog = reactive({
  visible: false,
})
const docs = reactive({
  id: "",
  title: "",
  content: "",
  ctx: "",
})
const file = ref(new FormData())

function init() {
  docs.id = props.ctx.id
  docs.title = props.ctx.title
  docs.content = props.ctx.content
  docs.ctx = docs.title + wrap + docs.content
}

function openDialog() {
  dialog.visible = true
  init()
}

function saveDocs() {
  // 数据处理
  const data = docs.ctx.split(wrap)
  docs.title = data[0]
  docs.content = ""
  if (data.length > 1) {
    const content = data.slice(1, data.length)
    docs.content = content.join(wrap)
  }
  emits("update:data", docs)
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
    }
    file.value = new FormData()
  })
}

defineExpose({
  openDialog,
})
</script>

<style lang="scss" scoped>
.on-editor {
  .v-note-wrapper {
    min-height: calc(100vh - 124px);
  }
}
</style>
