<template>
  <div class="document app-container">
    <el-menu
    @select="handleMenuSelect"
        :default-active="defaultActive"
        class="el-menu-vertical-demo"
        style="width: 250px;overflow: hidden;"
      >
        <el-menu-item :index="it.id" v-for="(it) in titleLists" :key="it.id" :title="it.title">
          <span class="titleStyle">{{it.title}}</span>
        </el-menu-item>
    </el-menu>
    <div style="flex: 1;padding-left: 20px;">
      <mavon-editor
      v-model="ctx"
      :subfield="false"
      :defaultOpen="'preview'"
      :toolbarsFlag="false"
      :boxShadow="false"
      placeholder="空"
    ></mavon-editor>
    </div>
  </div>
</template>

<script setup>
import { titleList,documentCommon } from '@/api/document/index.js'
//文档列表
let titleLists = ref([])
let defaultActive=ref('')
async function getTitleList(){
  let { data } = await titleList()
  titleLists.value = data
  defaultActive.value = data[0].id
  getDocument(defaultActive.value)
}
onMounted(() => {
  getTitleList()
})
let docs = reactive({
  title: '',
  content:''
})
const ctx = computed(() => docs.title + '\n' + docs.content)
async function getDocument(id){
  let { data } = await documentCommon(id)
  docs.title=data.title
  docs.content=data.content
}
function handleMenuSelect(index) {
  console.log('index', index);
  getDocument(index)
}
</script>

<style lang="scss" scoped>
.document{
  display: flex;
}
.titleStyle{
  white-space: nowrap; /* 设置文字在一行显示，不能换行 */
  overflow: hidden; /* 文字长度超出限定宽度，则隐藏超出的内容 */
  text-overflow: ellipsis; /* 规定当文本溢出时，显示省略符号来代表被修剪的文本 */}
</style>
