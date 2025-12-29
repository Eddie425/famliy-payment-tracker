# Swagger UI 預覽 - 我們建立的 API 文檔

## 📋 概覽

我們已經為整個專案建立了完整的 Swagger/OpenAPI 文檔，包括：

### 1. **OpenAPI 配置** (`OpenApiConfig.java`)
- API 標題：**Family Payment Tracker API**
- 版本：1.0.0
- 描述：包含功能介紹和認證說明
- 服務器：http://localhost:8080

### 2. **三個 API 分組（Tags）**

---

## 🔧 **Admin APIs** - 管理員 API

### POST `/api/admin/debts`
**創建新債務**
- **描述**：創建新債務並自動生成分期付款
- **請求範例**：
```json
{
  "title": "Car Loan",
  "totalAmount": 300000,
  "installmentCount": 12,
  "startDate": "2024-01-01",
  "interestRate": 5.5
}
```
- **回應**：201 Created - DebtResponseDTO

### GET `/api/admin/debts`
**獲取所有債務**
- **描述**：檢索所有債務，可選狀態篩選
- **查詢參數**：
  - `status` (可選)：ACTIVE 或 PAID_OFF
  - `includeInstallments` (默認：false)：是否包含分期詳情
- **回應**：200 OK - List<DebtResponseDTO>

### GET `/api/admin/debts/{id}`
**根據 ID 獲取債務**
- **描述**：獲取特定債務的詳細信息
- **路徑參數**：`id` - 債務 ID
- **查詢參數**：`includeInstallments` (默認：true)
- **回應**：200 OK - DebtResponseDTO

### GET `/api/admin/debts/{id}/installments`
**獲取債務的分期付款**
- **描述**：獲取特定債務的所有分期付款明細
- **回應**：200 OK - List<InstallmentResponseDTO>

### ⭐ PUT `/api/admin/installments/{id}`
**更新分期付款（調整每月付款）** - **主要功能**
- **描述**：更新特定分期付款的金額和/或到期日
- **請求範例**：
```json
{
  "amount": 30000,        // 可選
  "dueDate": "2024-04-20" // 可選
}
```
- **回應**：200 OK - InstallmentResponseDTO

### PUT `/api/admin/installments/bulk`
**批量更新分期付款**
- **描述**：一次性更新多個分期付款
- **請求範例**：
```json
[
  {
    "installmentId": 4,
    "amount": 30000,
    "dueDate": "2024-04-20"
  },
  {
    "installmentId": 5,
    "amount": 30000,
    "dueDate": "2024-05-20"
  }
]
```

### DELETE `/api/admin/debts/{id}`
**刪除債務**
- **描述**：刪除債務及其所有分期付款
- **回應**：200 OK - ApiResponseMessage

---

## 📊 **Dashboard APIs** - 儀表板 API

### GET `/api/dashboard/summary`
**獲取儀表板摘要**
- **描述**：返回完整的付款統計，包括總計、月度分解和視覺化數據
- **查詢參數**：
  - `year` (可選)：年份
  - `month` (可選)：月份 (1-12)
- **回應包含**：
  - 摘要信息（總已付、總未付、進度百分比）
  - 月度分解
  - 債務分解
  - 視覺化數據（圖表數據、進度條數據）

### GET `/api/dashboard/monthly`
**獲取月度分解**
- **描述**：獲取特定月份的詳細付款信息
- **查詢參數**：
  - `year` (必填)：年份
  - `month` (必填)：月份 (1-12)
- **回應**：200 OK - MonthlyBreakdownDTO

---

## 💳 **Payment APIs** - 付款 API

### GET `/api/payments`
**獲取付款狀態**
- **描述**：返回簡單的狀態消息，表示 API 正在運行
- **回應**：200 OK - Map with message and status

---

## 📝 **數據傳輸對象 (DTOs)**

所有 DTOs 都包含完整的 Swagger Schema 註解：

### CreateDebtRequest
- `title` (必填, 最大 200 字符)
- `totalAmount` (必填, 正數)
- `installmentCount` (必填, 最小 1)
- `startDate` (必填, ISO 格式)
- `interestRate` (可選, >= 0)

### UpdateInstallmentRequest
- `amount` (可選, 正數)
- `dueDate` (可選, ISO 格式)

### DebtResponseDTO
包含完整的債務信息，包括：
- 基本信息（id, title, totalAmount 等）
- 狀態（ACTIVE/PAID_OFF）
- 可選的分期付款列表
- 摘要統計（已付金額、剩餘金額等）

### InstallmentResponseDTO
包含完整的分期付款信息：
- 分期編號、金額、到期日
- 付款狀態（已付/未付）
- 是否逾期
- 付款日期

### DashboardSummaryDTO
包含完整的儀表板數據：
- 摘要統計
- 月度分解列表
- 債務分解列表
- 視覺化數據（用於圖表和進度條）

---

## 🎨 **Swagger UI 功能**

當應用程序運行時，Swagger UI 提供：

1. **互動式 API 測試**
   - 點擊 "Try it out" 按鈕
   - 填寫參數
   - 直接測試 API
   - 查看實時回應

2. **完整的文檔**
   - 每個端點的詳細描述
   - 參數說明和範例
   - 請求/回應 Schema
   - 錯誤代碼說明

3. **Schema 瀏覽器**
   - 查看所有 DTOs
   - 查看字段類型和要求
   - 範例值

4. **組織清晰**
   - 按標籤分組（Admin APIs, Dashboard APIs, Payment APIs）
   - 易於導航
   - 搜尋功能

---

## 🚀 **如何訪問**

一旦應用程序成功啟動：

1. **Swagger UI**: http://localhost:8080/swagger-ui.html
2. **OpenAPI JSON**: http://localhost:8080/v3/api-docs
3. **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

---

## 📸 **Swagger UI 預期畫面**

當你成功打開 Swagger UI 時，你會看到：

1. **頂部**：API 標題和描述
2. **左側**：API 分組列表
   - Admin APIs
   - Dashboard APIs
   - Payment APIs
3. **中間**：展開的 API 端點列表
   - 每個端點顯示 HTTP 方法、路徑、描述
   - "Try it out" 按鈕
   - 參數表格
   - 回應範例
4. **右側**：Schema 定義

---

## 🔍 **故障排除**

如果無法訪問 Swagger UI：

1. **確認應用程序正在運行**
   ```powershell
   # 檢查端口是否被使用
   netstat -ano | Select-String ":8080"
   ```

2. **檢查終端輸出**
   - 尋找 "Started FamliyPaymentTrackerApplication"
   - 檢查是否有錯誤訊息

3. **確認數據庫正在運行**
   ```powershell
   docker ps
   # 應該看到 family-payment-postgres 容器
   ```

4. **重新啟動應用程序**
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

---

## ✨ **我們建立的所有內容總結**

✅ OpenAPI 配置類
✅ 3 個控制器類（Admin, Dashboard, Payment）
✅ 完整的 Swagger 註解（@Operation, @ApiResponse, @Parameter）
✅ 所有 DTOs 的 Schema 註解
✅ API 分組（Tags）
✅ 詳細的描述和範例
✅ 互動式測試功能

這就是我們為你的專案建立的完整 Swagger API 文檔系統！













