# 快速部署指南

## 🚀 最简单的部署方式（Railway + Vercel）

### 第一步：部署后端（Railway）

1. **访问 Railway**：https://railway.app
2. **登录**：使用 GitHub 账号登录
3. **创建项目**：
   - 点击 "New Project"
   - 选择 "Deploy from GitHub repo"
   - 选择 `famliy-payment-tracker` 仓库
4. **添加数据库**：
   - 点击 "+ New" → "Database" → "PostgreSQL"
   - 等待数据库创建完成
5. **配置环境变量**（Railway 通常会自动配置，但请检查）：
   - 点击服务 → "Variables"
   - 确保有以下变量（Railway 通常会自动从数据库服务注入）：
     ```
     SPRING_DATASOURCE_URL=${{Postgres.DATABASE_URL}}
     SPRING_DATASOURCE_USERNAME=${{Postgres.USERNAME}}
     SPRING_DATASOURCE_PASSWORD=${{Postgres.PASSWORD}}
     ```
6. **获取后端 URL**：
   - 部署完成后，在服务设置中找到 "Settings" → "Generate Domain"
   - 复制生成的 URL（例如：https://family-payment-tracker-production.up.railway.app）

### 第二步：部署前端（Vercel）

1. **访问 Vercel**：https://vercel.com
2. **登录**：使用 GitHub 账号登录
3. **创建项目**：
   - 点击 "New Project"
   - 导入 `famliy-payment-tracker-web` 仓库
4. **配置环境变量**：
   - 在 "Environment Variables" 中添加：
     ```
     VITE_API_BASE_URL=https://your-backend-url.railway.app
     ```
   - 将 `https://your-backend-url.railway.app` 替换为第一步中获得的后端 URL
5. **部署**：
   - 点击 "Deploy"
   - 等待部署完成

### 第三步：完成！

- 前端 URL：Vercel 会提供一个 URL（例如：https://your-app.vercel.app）
- 现在您可以在任何设备上访问这个 URL 使用应用！

---

## 📝 注意事项

1. **免费额度**：
   - Railway：每月 $5 免费额度（通常足够小型应用使用）
   - Vercel：个人项目完全免费

2. **数据库迁移**：
   - Flyway 会在应用启动时自动运行数据库迁移
   - 首次部署时会自动创建表结构

3. **环境变量**：
   - 确保前端的环境变量 `VITE_API_BASE_URL` 指向正确的后端 URL
   - 如果后端 URL 改变，需要更新前端环境变量并重新部署

4. **CORS**：
   - 后端已经配置了 CORS，允许来自 Vercel 和 Netlify 的请求
   - 如果需要添加其他域名，需要修改 `CorsConfig.java`

---

## 🔄 更新应用

当您更新代码后：

1. **后端更新**：
   - 推送到 GitHub
   - Railway 会自动检测并重新部署

2. **前端更新**：
   - 推送到 GitHub
   - Vercel 会自动检测并重新部署

---

## ❓ 遇到问题？

1. **后端无法启动**：
   - 检查环境变量是否正确配置
   - 查看 Railway 的日志（Logs 标签）

2. **前端无法连接后端**：
   - 确认 `VITE_API_BASE_URL` 环境变量正确
   - 检查浏览器控制台的错误信息
   - 确认后端 CORS 配置允许前端域名

3. **数据库连接失败**：
   - 确认数据库服务正在运行
   - 检查环境变量是否正确注入

---

祝部署顺利！🎉
