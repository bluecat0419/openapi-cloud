## 开发

node版本：16.18.1

npm版本：8.19.2

```bash
# 克隆项目
git clone https://github.com/bluecat0419/openapi-cloud.git

# 进入项目目录
cd openapi_frontend/xxx-stack

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npmmirror.com

# 启动服务
npm run dev
```

浏览器访问 http://localhost:端口号

## 发布

```bash

# 构建生产环境
npm run build:prod
```
