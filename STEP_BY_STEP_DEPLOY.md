# 一步步部署指南 🚀

## 准备工作

### 1. 确保代码已推送到 GitHub

在开始部署之前，请确保您的代码已推送到 GitHub：

```bash
# 后端仓库
cd "d:\side project\famliy-payment-tracker"
git add .
git commit -m "Add deployment configuration"
git push origin main  # 或 master，取决于您的分支名

# 前端仓库
cd "d:\side project\famliy-payment-tracker-web"
git add .
git commit -m "Add deployment configuration"
git push origin main  # 或 master，取决于您的分支名
```

---

## 第一部分：部署后端到 Railway

### 步骤 1：访问 Railway 并登录

1. 打开浏览器，访问：**https://railway.app**
2. 点击右上角的 **"Login"** 或 **"Start a New Project"**
3. 选择 **"Login with GitHub"**
4. 授权 Railway 访问您的 GitHub 账号

### 步骤 2：创建新项目

1. 登录后，点击 **"New Project"** 按钮（通常在页面右上角或中间）
2. 选择 **"Deploy from GitHub repo"**
3. 在仓库列表中，找到并选择 **`famliy-payment-tracker`**
4. Railway 会自动开始构建（第一次构建可能需要 5-10 分钟）

### 步骤 3：添加 PostgreSQL 数据库

1. 在项目页面，点击 **"+ New"** 按钮（通常在左侧或顶部）
2. 选择 **"Database"**
3. 选择 **"PostgreSQL"**
4. 等待数据库创建完成（约 1-2 分钟）

### 步骤 4：配置环境变量

1. 点击您的后端服务（通常是 `famliy-payment-tracker` 或类似的名称）
2. 点击 **"Variables"** 标签
3. Railway 通常会**自动**从 PostgreSQL 数据库服务注入以下变量：
   - `DATABASE_URL`
   - `PGHOST`
   - `PGPORT`
   - `PGDATABASE`
   - `PGUSER`
   - `PGPASSWORD`

4. **如果自动注入的变量格式不对**，请手动添加以下环境变量：
   ```
   SPRING_DATASOURCE_URL=${{Postgres.DATABASE_URL}}
   SPRING_DATASOURCE_USERNAME=${{Postgres.USERNAME}}
   SPRING_DATASOURCE_PASSWORD=${{Postgres.PASSWORD}}
   ```

   > 注意：`${{Postgres.XXX}}` 是 Railway 的变量引用语法，会自动从 PostgreSQL 服务获取值

### 步骤 5：生成公共域名

1. 点击后端服务的 **"Settings"** 标签
2. 找到 **"Generate Domain"** 部分
3. 点击 **"Generate Domain"** 按钮
4. Railway 会生成一个类似这样的 URL：
   ```
   https://famliy-payment-tracker-production-xxxx.up.railway.app
   ```
5. **复制这个 URL**，稍后会用到！

### 步骤 6：验证后端部署

1. 等待构建完成（查看 "Deployments" 标签中的状态）
2. 构建成功后，点击生成的域名链接
3. 应该能看到 Swagger UI 或 API 响应
4. 测试 Swagger：在 URL 后面添加 `/swagger-ui.html`
   ```
   https://your-app.up.railway.app/swagger-ui.html
   ```

---

## 第二部分：部署前端到 Vercel

### 步骤 1：访问 Vercel 并登录

1. 打开浏览器，访问：**https://vercel.com**
2. 点击右上角的 **"Sign Up"** 或 **"Login"**
3. 选择 **"Continue with GitHub"**
4. 授权 Vercel 访问您的 GitHub 账号

### 步骤 2：创建新项目

1. 登录后，点击 **"Add New..."** → **"Project"**
2. 在仓库列表中，找到并选择 **`famliy-payment-tracker-web`**
3. 点击 **"Import"**

### 步骤 3：配置项目

1. **Framework Preset**：应该自动检测为 "Vite"（如果不是，手动选择）
2. **Root Directory**：保持默认（`./`）
3. **Build Command**：`npm run build`（应该已自动填充）
4. **Output Directory**：`dist`（应该已自动填充）

### 步骤 4：添加环境变量

1. 在 "Environment Variables" 部分，点击 **"Add"**
2. 添加以下环境变量：
   - **Key**: `VITE_API_BASE_URL`
   - **Value**: 粘贴您在 Railway 获得的后端 URL（例如：`https://famliy-payment-tracker-production-xxxx.up.railway.app`）
   - **Environment**: 选择所有环境（Production, Preview, Development）

3. 点击 **"Add"** 保存

### 步骤 5：部署

1. 点击页面底部的 **"Deploy"** 按钮
2. 等待构建完成（通常需要 1-3 分钟）
3. 部署成功后，Vercel 会显示一个类似这样的 URL：
   ```
   https://famliy-payment-tracker-web.vercel.app
   ```
4. **复制这个 URL**，这就是您的前端访问地址！

---

## 第三部分：验证部署

### 测试前端

1. 打开前端 URL（Vercel 提供的链接）
2. 应该能看到应用界面
3. 尝试访问 Dashboard 页面
4. 打开浏览器开发者工具（F12）→ Console 标签
5. 检查是否有 API 错误

### 如果前端无法连接后端

1. 确认 `VITE_API_BASE_URL` 环境变量正确
2. 如果后端 URL 有变化，需要：
   - 在 Vercel 项目设置中更新环境变量
   - 触发重新部署（可以推送一个小的更改到 GitHub，或手动触发重新部署）

### 测试后端 API

1. 在浏览器中访问后端 Swagger UI：
   ```
   https://your-backend-url.railway.app/swagger-ui.html
   ```
2. 尝试调用一些 API 端点，确认后端正常工作

---

## 常见问题解决

### 问题 1：Railway 构建失败

**可能原因**：
- Dockerfile 有错误
- 依赖下载失败

**解决方法**：
1. 点击构建日志查看详细错误信息
2. 检查 Dockerfile 是否正确
3. 确认代码已正确推送到 GitHub

### 问题 2：后端无法连接数据库

**可能原因**：
- 环境变量未正确配置
- 数据库服务未启动

**解决方法**：
1. 检查环境变量是否正确设置
2. 确认 PostgreSQL 服务正在运行（在 Railway 项目中应该能看到）
3. 查看后端日志（Railway → 服务 → Logs）

### 问题 3：前端显示 "Failed to load" 或 CORS 错误

**可能原因**：
- 后端 URL 不正确
- CORS 配置问题

**解决方法**：
1. 确认 `VITE_API_BASE_URL` 环境变量指向正确的后端 URL
2. 确认后端 URL 可以访问（在浏览器中直接打开）
3. 检查后端 CORS 配置是否包含 Vercel 域名

### 问题 4：环境变量更新后未生效

**解决方法**：
- 在 Vercel 中更新环境变量后，需要重新部署
- 可以在 "Deployments" 标签中手动触发重新部署，或推送一个小更改到 GitHub

---

## 完成！🎉

如果一切顺利，您现在应该：
- ✅ 后端运行在 Railway 上
- ✅ 前端运行在 Vercel 上
- ✅ 可以在任何设备上通过前端 URL 访问应用

---

## 后续更新

当您更新代码时：

1. **推送代码到 GitHub**
2. **Railway 会自动检测并重新部署后端**
3. **Vercel 会自动检测并重新部署前端**

无需任何额外操作！🚀
