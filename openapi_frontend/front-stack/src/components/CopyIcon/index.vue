<template>
  <el-tooltip class="box-item" effect="light" content="copy" placement="top">
    <el-icon style="margin-left: 6px; cursor: pointer" @click="copyToClipboard">
      <DocumentCopy />
    </el-icon>
  </el-tooltip>
</template>

<script setup lang="ts">
import { ref, reactive, toRefs, watch, onMounted, getCurrentInstance, isProxy, toRaw } from "vue"
import { ElMessage } from "element-plus"

import { useRouter } from "vue-router"
const router = useRouter()
const props = defineProps({
  value: {
    type: String,
    default: "",
  },
})
const copyToClipboard = function () {
  console.log(props.value)
  let text = props.value
  if (isProxy(text)) {
    text = JSON.stringify(toRaw(text))
  }
  console.log(text)
  const input = document.createElement("textarea")
  input.value = text
  document.body.appendChild(input)
  input.select()
  document.execCommand("copy")
  document.body.removeChild(input)
  ElMessage({
    message: "复制成功",
    type: "success",
    duration: 800,
  })
}

const emit = defineEmits([""])

//const { } = toRefs(props);

const { data } = toRefs(
  reactive({
    //定义数组和对象
    data: "",
  }),
)
</script>
<style lang="scss" scoped></style>
