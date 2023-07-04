<template>
  <div v-if="!isModify" class="text">
    <span v-if="show">{{ data["value"] }}</span>
    <el-tag v-else type="warning" style="cursor: pointer">{{ props.empty }}</el-tag>
    <svg-icon icon-class="pencil" @click="openInput" class-name="modify-icon" />
  </div>
  <div v-else class="modify-input">
    <template v-if="type === 'el-input'">
      <el-input v-model="data['current']"></el-input>
    </template>
    <template v-else-if="type === 'el-select'">
      <el-select v-model="data['current']">
        <el-option
          v-for="item in data['options']"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </template>
    <el-button size="small" type="primary" text @click="updateInfo">保存</el-button>
    <el-button size="small" text @click="closeInput">取消</el-button>
  </div>
</template>

<script setup>
const props = defineProps({
  type: {
    type: String,
    default: "input",
  },
  data: {
    type: Object,
    required: true,
  },
  options: {
    type: Map,
    default: new Map([]),
  },
  empty: {
    type: String,
    default: "未设置",
  },
})
const emit = defineEmits(["update"])

const show = computed(() => Object.values(props.data)[0])
const isModify = ref(false)

const type = computed(() => {
  return `el-${props.type}`
})
const data = computed(() => {
  const data = ref({})
  const key = Object.keys(props.data)[0]
  const value = Object.values(props.data)[0]
  data.value["key"] = key
  data.value["value"] = ref(value)
  data.value["current"] = ref(value)
  if (props.type === "select") {
    if (!isModify.value) {
      data.value["value"] = props.options.get(data.value["value"])
    }
    data.value["options"] = []
    for (let [key, value] of props.options) {
      data.value["options"].push({ label: value, value: key })
    }
  }
  return data.value
})

function closeInput() {
  isModify.value = false
}

function openInput() {
  isModify.value = true
}

function updateInfo() {
  if (data.value["current"] !== data.value["value"]) {
    const updateData = ref({})
    updateData.value[data.value.key] = data.value["current"]
    emit("update", updateData.value)
    nextTick(() => {
      // 下一次DOM更新时，更新data的value值
      data.value["value"] = data.value["current"]
    })
  }
  closeInput()
}
</script>

<style lang="scss" scoped>
.text,
.modify-input {
  display: inline-block;
}

.text {
  .modify-icon {
    opacity: 0;
    margin-left: 5px;
    cursor: pointer;
  }

  &:hover {
    .modify-icon {
      opacity: 1;
    }
  }
}

.modify-input {
  width: 70%;
  .el-input,
  .el-select {
    width: 60%;
  }
  .el-button {
    margin-left: 0;
    width: 20%;
  }
}
</style>
