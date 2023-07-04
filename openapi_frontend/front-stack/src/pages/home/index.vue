<template>
  <div class="home main-container" style="position: relative">
    <div style="width: 600px; margin: 0 auto">
      <el-carousel height="150px">
        <el-carousel-item v-for="item in 4" :key="item">
          <h3 class="small justify-center" text="2xl">{{ item }}</h3>
        </el-carousel-item>
      </el-carousel>
    </div>
    <h3 style="text-align: center">接口列表</h3>
    <div class="interface" style="margin: 0 150px">
      <div class="interface-filter" style="width: 550px; margin-left: auto">
        <div class="sort" style="display: inline-block">
          <span>排序：</span>
          <el-select v-model="requestParams.sortType" @change="selectChange">
            <el-option
              v-for="item in sortOption"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </div>
        <div class="search" style="display: inline-block; margin-left: 30px">
          <el-input
            v-model="requestParams.name"
            placeholder="请输入接口名称关键词"
            @change="clickSearch"
          >
            <template #suffix>
              <svg-icon icon-class="search" @click="clickSearch"></svg-icon>
            </template>
          </el-input>
        </div>
      </div>
      <div class="interface-wrapper">
        <interface-item :data-item="interfaceInfo.interfaceList"></interface-item>
      </div>
    </div>
    <Pagination
      :current="paginationData.current"
      :total="paginationData.total"
      @changePage="handleChangePage"
    ></Pagination>
  </div>
</template>

<script setup>
import interfaceItem from "./interfaceItem.vue"
import Pagination from "@/components/Pagination/index.vue"
import { getHomePage } from "@/api/home"
onMounted(() => {
  handleHomePage()
})
let requestParams = reactive({
  name: "",
  sortType: 0,
  limit: 8,
})
const sortOption = [
  {
    value: 0,
    label: "最新",
  },
  {
    value: 1,
    label: "评分",
  },
  {
    value: 2,
    label: "成交量",
  },
]
let interfaceInfo = reactive({
  interfaceList: [],
})
const paginationData = reactive({
  current: "1",
  total: "1",
})
async function handleHomePage() {
  let { data } = await getHomePage({ ...requestParams, current: paginationData.current })
  interfaceInfo.interfaceList = data.list
  paginationData.total = Math.ceil(data.total / requestParams.limit)
}

const selectChange = function () {
  paginationData.current = "1"
  handleHomePage()
}
const clickSearch = function () {
  paginationData.current = "1"
  handleHomePage()
}
const handleChangePage = function (page) {
  paginationData.current = page
  handleHomePage()
}
</script>

<style lang="scss" scoped>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.interface-wrapper {
  margin: 10px auto;
}
</style>
