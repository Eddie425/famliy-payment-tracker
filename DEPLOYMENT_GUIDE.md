# 部署指南 (Deployment Guide)

本指南将帮助您将 Family Payment Tracker 应用部署到云端，使其可以从任何地方访问。

## 推荐的部署方案

### 方案 1: Railway（推荐，最简单）

Railway 是一个现代化的部署平台，可以同时部署后端和数据库。

#### 后端部署步骤：

1. **注册 Railway 账号**
   - 访问 https://railway.app
   - 使用 GitHub 账号登录

2. **部署后端**
   - 点击 "New Project"
   - 选择 "Deploy from GitHub repo"
   - 选择您的后端仓库 (`famliy-payment-tracker`)
   - Railway 会自动检测 Dockerfile 并开始构建

3. **添加 PostgreSQL 数据库**
   - 在项目中点击 "+ New"
   - 选择 "Database" → "PostgreSQL"
   - Railway 会自动创建数据库并设置环境变量

4. **配置环境变量**
   - 在项目设置中找到 "Variables" 标签
   - 添加以下环境变量：
     ```
     SPRING_DATASOURCE_URL=${{Postgres.DATABASE_URL}}
     SPRING_DATASOURCE_USERNAME=${{Postgres.USERNAME}}
     SPRING_DATASOURCE_PASSWORD=${{Postgres.PASSWORD}}
     PORT=8080
     ```
   - Railway 会自动从数据库服务注入这些值

5. **获取后端 URL**
   - 部署完成后，Railway 会提供一个 URL（例如：https://your-app.up.railway.app）
   - 复制这个 URL，稍后用于前端配置

---

### 方案 2: Render

Render 提供免费的 PostgreSQL 数据库和 Web 服务。

#### 后端部署步骤：

1. **注册 Render 账号**
   - 访问 https://render.com
   - 使用 GitHub 账号登录

2. **创建 PostgreSQL 数据库**
   - 点击 "New +" → "PostgreSQL"
   - 填写信息：
     - Name: `family-payment-postgres`
     - Database: `family_payment`
     - User: `fpt`
     - Region: 选择离您最近的区域
   - 点击 "Create Database"
   - 等待数据库创建完成

3. **部署后端服务**
   - 点击 "New +" → "Web Service"
   - 连接您的 GitHub 仓库 (`famliy-payment-tracker`)
   - 配置：
     - Name: `family-payment-tracker-api`
     - Environment: `Docker`
     - Region: 与数据库相同的区域
   - 在 Environment Variables 中添加：
     ```
     SPRING_DATASOURCE_URL=jdbc:postgresql://[数据库主机]:5432/family_payment
     SPRING_DATASOURCE_USERNAME=fpt
     SPRING_DATASOURCE_PASSWORD=[数据库密码]
     PORT=8080
     ```
   - 点击 "Create Web Service"

4. **获取后端 URL**
   - 部署完成后，Render 会提供一个 URL（例如：https://family-payment-tracker-api.onrender.com）
   - 复制这个 URL

---

## 前端部署

### 方案 1: Vercel（推荐）

Vercel 非常适合部署 React 应用，提供免费的 HTTPS 和全球 CDN。

#### 部署步骤：

1. **注册 Vercel 账号**
   - 访问 https://vercel.com
   - 使用 GitHub 账号登录

2. **部署前端**
   - 点击 "New Project"
   - 导入您的前端仓库 (`famliy-payment-tracker-web`)
   - 在 Environment Variables 中添加：
     ```
     VITE_API_BASE_URL=https://your-backend-url.railway.app
     ```
   - 点击 "Deploy"

3. **获取前端 URL**
   - 部署完成后，Vercel 会提供一个 URL（例如：https://your-app.vercel.app）
   - 这个就是您的前端访问地址

---

### 方案 2: Netlify

Netlify 也提供类似的部署服务。

#### 部署步骤：

1. **注册 Netlify 账号**
   - 访问 https://www.netlify.com
   - 使用 GitHub 账号登录

2. **部署前端**
   - 点击 "Add new site" → "Import an existing project"
   - 连接您的前端仓库
   - 构建设置：
     - Build command: `npm run build`
     - Publish directory: `dist`
   - 在 Environment Variables 中添加：
     ```
     VITE_API_BASE_URL=https://your-backend-url.railway.app
     ```
   - 点击 "Deploy site"

---

## 环境变量配置

### 后端环境变量

后端应用需要以下环境变量：

```bash
# 数据库连接（Railway/Render 会自动提供）
SPRING_DATASOURCE_URL=jdbc:postgresql://host:port/database
SPRING_DATASOURCE_USERNAME=username
SPRING_DATASOURCE_PASSWORD=password

# 服务端口（通常由平台自动设置）
PORT=8080
```

### 前端环境变量

前端应用需要：

```bash
# API 后端地址
VITE_API_BASE_URL=https://your-backend-url.railway.app
```

---

## 更新 application.yaml 以支持环境变量

我们已经创建了支持环境变量的配置文件。应用会自动从环境变量读取数据库配置。

如果使用 Railway，环境变量会自动注入。如果使用其他平台，请确保设置了正确的环境变量。

---

## CORS 配置

确保后端允许前端域名访问。CorsConfig 已经配置为允许所有来源，在生产环境中您可能需要限制为特定域名。

---

## 免费方案限制

### Railway
- 免费层：$5/月 信用额度
- 适合小到中型应用

### Render
- Web 服务：免费，但会在 15 分钟无活动后休眠
- 数据库：免费 90 天，之后需要付费

### Vercel
- 完全免费用于个人项目
- 无限的带宽和构建

### Netlify
- 免费层有带宽限制
- 适合个人项目

---

## 部署后的步骤

1. ✅ 确保后端成功部署并运行
2. ✅ 确保数据库连接正常
3. ✅ 部署前端并配置正确的 API URL
4. ✅ 测试前端是否能正常访问后端 API
5. ✅ 测试所有功能（创建债务、查看详情等）

---

## 故障排除

### 后端无法连接数据库
- 检查环境变量是否正确设置
- 确认数据库服务已启动
- 检查数据库连接字符串格式

### 前端无法访问后端 API
- 检查 `VITE_API_BASE_URL` 是否正确
- 检查后端 CORS 配置
- 查看浏览器控制台的错误信息

### 构建失败
- 检查 Dockerfile 是否正确
- 确认所有依赖都已包含
- 查看构建日志了解详细错误

---

## 更新应用

当您更新代码后：

1. **后端**：推送到 GitHub，Railway/Render 会自动重新部署
2. **前端**：推送到 GitHub，Vercel/Netlify 会自动重新部署

---

祝您部署顺利！🚀
