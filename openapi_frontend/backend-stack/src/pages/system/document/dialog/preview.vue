<template>
  <el-dialog custom-class="on-preview" v-model="dialog.visible" center align-center destroy-on-close>
    <template #header>
      <b>预览文档</b>
    </template>
    <mavon-editor
      v-model="ctx"
      :subfield="false"
      :defaultOpen="'preview'"
      :toolbarsFlag="false"
      :boxShadow="false"
      placeholder="空"
    ></mavon-editor>
  </el-dialog>
</template>

<script setup>
import { wrap } from "./common"

const props = defineProps({
  ctx: {
    type: Object,
    default: { id: "", title: "", content: "" },
  },
})

const dialog = reactive({
  visible: false,
})
const docs = reactive({
  title: "",
  content: "",
})
const ctx = computed(() => docs.title + wrap + docs.content)

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

defineExpose({
  openDialog,
})
</script>

<style lang="scss" scoped></style>
