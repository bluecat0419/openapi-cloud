<template>
  <el-row class="interface-item" justify="space-evenly">
    <div class="interface-item-li" v-for="interfaceItem in dataItem">
      <el-card shadow="hover">
        <template #header>
          <img width="50" :src="interfaceItem.icon || interfaceImg" class="header-logo" />

          <div class="header-right">
            <header>
              <b>{{ interfaceItem.name }}</b>
            </header>
            <el-rate v-model="interfaceItem.score" size="small" class="interface_rate" disabled />
            <span :title="interfaceItem.description">{{ interfaceItem.description }}</span>
          </div>
        </template>

        <header class="content-header">
          <b>{{ interfaceItem.price }}</b>
        </header>
        <el-row justify="space-evenly" class="content-option">
          <el-col :span="11">
            <el-button size="small" @click="toInterfaceDetail(interfaceItem.id)">
              查看详情
            </el-button>
          </el-col>
          <el-col :span="11">
            <span class="interface-view">已成交：{{ interfaceItem.dealCount }}单</span>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </el-row>
</template>

<script setup>
const router = useRouter()
const props = defineProps({
  dataItem: {
    type: Array,
    default: [],
  },
})
import interfaceImg from "@/assets/images/interfaceImg.jpg"
const dataItem = computed(() => props.dataItem)

function toInterfaceDetail(id) {
  router.push({
    path: "/interface/info",
    query: { id },
  })
}
</script>

<style lang="scss" scoped>
@mixin FontThickness($size, $weight) {
  font-size: $size;
  font-weight: $weight;
}

:deep(.el-card__body) {
  padding: 10px !important;
}
.interface-item-li {
  width: 300px;
  margin: 10px 20px 30px;
}
.header-logo {
  border-radius: 50%;
  height: 50px;
}
.header-right {
  display: inline-block;
  margin-left: 5px;
  max-width: calc(100% - 50px);
  .el-rate {
    display: block;
  }

  span {
    display: block;
    height: 40px;
    width: 200px;
    white-space: wrap;
    overflow: hidden;
    text-overflow: ellipsis; /* 规定当文本溢出时，显示省略符号来代表被修剪的文本 */
    @include FontThickness(14px, 300);
  }
}
.content-header {
  margin-bottom: 5px;
  color: coral;
}
.content-option {
  .interface-view {
    @include FontThickness(14px, 300);
    line-height: 24px;
    color: chocolate;
  }
}
</style>
