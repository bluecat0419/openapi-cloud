<template>
  <div class="pagination_item">
    <el-button class="previous page_both" @click="clickChangePage('-1')" :disabled="current == '1'">
      上一页
    </el-button>
    <div class="current_total">{{ current }}/{{ total }}</div>
    <el-button class="next page_both" :disabled="current == total" @click="clickChangePage('1')">
      下一页
    </el-button>
  </div>
</template>

<script setup>
const emit = defineEmits(["changePage"])
const props = defineProps({
  current: {
    type: String,
    default: "1",
  },
  total: {
    type: String,
    default: "1",
  },
})

const { data } = toRefs(
  reactive({
    //定义数组和对象
    data: "",
  }),
)

const clickChangePage = function (type) {
  let page = props.current
  type === "1" ? (page = page * 1 + 1) : (page = page * 1 - 1)
  emit("changePage", page)
}
</script>
<style lang="scss" scoped>
.pagination_item {
  width: 400px;
  margin: 10px auto;
  text-align: center;

  .page_both {
    border: 1px solid #ccc;
    padding: 2px 4px;
  }
  .current_total {
    display: inline-block;
    margin: 0 18px;
  }
}
</style>
