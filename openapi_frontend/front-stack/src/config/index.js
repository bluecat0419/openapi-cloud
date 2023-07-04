/**
 * 环境配置
 */
const env = import.meta.env.MODE || "production"

const EnvConfig = {
  // 这里的开发环境 的 BaseApi 是经过代理的
  development: {
    BaseApi: "/dev-api",
    MockApi: "https://www.fastmock.site/mock/8aa325c62d70c5f4f5d9eb7c5ee7448f/mockfrontend",
  },
  test: {
    BaseApi: null,
    MockApi: null,
  },
  // 生成环境的 BaseApi 是不能经过代理的，需要实际的 url地址，下面只是个示例
  production: {
    BaseApi: "http://localhost:8000/cloud",
    MockApi: null,
  },
}

export default {
  env,
  mock: true,
  ...EnvConfig[env],
}
