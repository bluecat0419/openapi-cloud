import vue from "@vitejs/plugin-vue"
import AutoImportElementPlus from "./auto-import-elementPlus"
import createAutoImport from "./auto-import"
import createSvgIcon from "./svg-icon"

export default function createVitePlugins() {
  const vitePlugins = [vue()]
  vitePlugins.push(AutoImportElementPlus())
  vitePlugins.push(createAutoImport())
  vitePlugins.push(createSvgIcon())
  return vitePlugins.flat()
}
