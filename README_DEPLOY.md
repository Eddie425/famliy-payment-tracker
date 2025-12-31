# 部署说明

## 📦 已准备好的部署文件

本项目已经配置好所有必要的部署文件：

### 后端部署文件：
- ✅ `Dockerfile` - 用于容器化构建
- ✅ `.dockerignore` - 排除不需要的文件
- ✅ `railway.json` - Railway 平台配置
- ✅ `render.yaml` - Render 平台配置
- ✅ `application.yaml` - 支持环境变量的配置文件

### 前端部署文件：
- ✅ `vercel.json` - Vercel 部署配置
- ✅ `netlify.toml` - Netlify 部署配置

### 文档：
- 📖 `QUICK_DEPLOY.md` - 快速部署指南（推荐先看这个）
- 📖 `DEPLOYMENT_GUIDE.md` - 详细部署指南

---

## 🚀 推荐的部署流程

### 方案 A：Railway（后端）+ Vercel（前端） ⭐ 推荐

**优点**：
- 设置简单
- Railway 自动处理数据库和环境变量
- Vercel 免费且速度快
- 自动部署（GitHub 推送即部署）

**步骤**：
1. 查看 `QUICK_DEPLOY.md` 获取详细步骤
2. 后端部署到 Railway（约 5 分钟）
3. 前端部署到 Vercel（约 3 分钟）
4. 完成！

---

### 方案 B：Render（后端 + 数据库）+ Netlify（前端）

**优点**：
- Render 提供免费数据库
- Netlify 提供免费托管

**注意**：
- Render 免费服务在 15 分钟无活动后会休眠
- 首次访问需要等待约 30 秒启动

**步骤**：
1. 查看 `DEPLOYMENT_GUIDE.md` 获取详细步骤

---

## 🔑 重要环境变量

### 后端需要：
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://host:port/database
SPRING_DATASOURCE_USERNAME=username
SPRING_DATASOURCE_PASSWORD=password
PORT=8080  # 通常由平台自动设置
```

### 前端需要：
```bash
VITE_API_BASE_URL=https://your-backend-url.railway.app
```

---

## ✅ 部署前检查清单

- [ ] 代码已推送到 GitHub
- [ ] Dockerfile 已创建（✅ 已完成）
- [ ] 环境变量配置已更新（✅ 已完成）
- [ ] CORS 配置已更新（✅ 已完成）
- [ ] 准备部署平台账号（Railway/Vercel 或 Render/Netlify）

---

## 📚 详细文档

- **快速开始**：阅读 `QUICK_DEPLOY.md`
- **详细说明**：阅读 `DEPLOYMENT_GUIDE.md`
- **前端部署**：阅读前端仓库的 `DEPLOYMENT.md`

---

祝部署顺利！🎉
