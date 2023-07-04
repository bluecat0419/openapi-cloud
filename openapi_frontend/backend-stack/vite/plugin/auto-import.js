import AutoImport from "unplugin-auto-import/vite"

export default function createAutoImport() {
  return AutoImport({
    imports: ["vue", "vue-router", "pinia"],
    // 生成相应.d.ts文件的文件路径。 false代表禁用
    dts: false,
  })
}
