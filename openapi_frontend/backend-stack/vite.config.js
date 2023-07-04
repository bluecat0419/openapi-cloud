import { defineConfig, loadEnv } from "vite"
import path from "path"
import createVitePlugins from "./vite/plugin"

// https://vitejs.dev/config/
export default defineConfig(({ mode, command }) => {
  // process.cwd() => 项目根目录（index.html 文件所在的位置）。
  const env = loadEnv(mode, process.cwd())
  const { VITE_APP_ENV } = env
  return {
    // 部署生产环境和开发环境下的URL。
    // 默认情况下，vite 会假设你的应用是被部署在一个域名的根路径上
    // 例如 https://www.ruoyi.vip/。如果应用被部署在一个子路径上，你就需要用这个选项指定这个子路径。例如，如果你的应用被部署在 https://www.ruoyi.vip/admin/，则设置 baseUrl 为 /admin/。
    base: VITE_APP_ENV === "development" ? "/dev" : "",
    plugins: createVitePlugins(),
    resolve: {
      alias: {
        // 设置路径
        "~": path.resolve(__dirname, "./"),
        // 设置别名
        "@": path.resolve(__dirname, "./src"),
      },
      // 这里没有引入 .vue , 所以引入 .vue文件 的时候写全名哟~
      extensions: [".mjs", ".js", ".mts", ".ts", ".jsx", ".tsx", ".json"],
    },
    // vite 相关配置
    server: {
      port: 8000,
      host: true,
      open: true,
      // 为开发服务器配置自定义代理规则。
      proxy: {
        "/dev-api": {
          target: "http://localhost:8000/cloud",
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/dev-api/, ""),
        },
      },
    },
  }
})
