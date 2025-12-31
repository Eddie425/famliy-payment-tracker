# Family Payment Tracker（FPT）需求文件 & 開發計畫

## 0. 專案目的

建立一個前後端分離的小系統，用來管理家庭分期負債與每期扣款狀態，讓使用者（父母）可以「勾選當期已繳」，並能自動試算「總欠款金額」。

---

## 1. 使用者角色

### 1.1 Viewer（父母）

* 只看清單
* 只能勾選「當期已繳」
* 不可新增 / 修改 / 刪除款項

### 1.2 Admin（你）

* 新增負債（Debt）
* 產生分期排程（Installments）
* 必要時調整分期金額/日期
* 查看全局統計（總欠款、已繳、剩餘）

> MVP 可先不做登入，但 API 路徑要先區分 `/api/admin/**`，未來好加權限。

---

## 2. 核心資料模型（回顧）

* **Debt（負債主體）**：一筆合約（信貸/分期/車貸）
* **DebtInstallment（分期）**：每一期要繳的明細，可被勾選完成

---

## 3. MVP 功能需求（必做）

### 3.1 Viewer 端

* 查看「所有 ACTIVE 的分期清單」（可依到期日排序）
* 勾選某一期為已繳（Mark Paid）
* 顯示：

  * 當期是否已繳
  * 到期日是否逾期（未繳且 dueDate < today）

### 3.2 Admin 端

* 新增 Debt（含：總額、期數、開始日、利率可選）
* 自動產生 Installments（期數 N → N 筆）
* 查看某筆 Debt 的分期表
* 可調整某期（dueDate、amount）（MVP 允許手動調整，不做利息重算）
* 刪除 Debt（連同分期一併刪除 or 禁止刪除改為 inactive，二選一）

### 3.3 統計（必做）

* 計算：

  * **Total Outstanding（總欠款）**：所有未繳 installment.amount 的總和（只算 ACTIVE）
  * **Total Paid（已繳）**：所有已繳 installment.amount 的總和
* Debt 若所有 installments 都 paid → Debt.status 自動變 `PAID_OFF`

---

## 4. 非功能需求（MVP）

* 後端：Spring Boot 3.4.x、Java 17、PostgreSQL、Flyway
* API：REST JSON
* Migration：不得使用 `ddl-auto=create/update`，只能 `validate`
* 可本機 Docker 一鍵啟動 DB

---

## 5. 明確不做（Scope Control）

* 提前還款 / 罰息
* 多幣別
* 複雜 amortization 公式（利息重算）
* 登入/權限（但保留 admin path）

---

# 6. 交付拆分（Notion 可打勾的步驟清單）

## Milestone A：環境初始化（Backend Foundation）

* [x] A1. 建立 Spring Boot 專案（Maven, Java 17, Boot 3.4.x）
* [x] A2. 加入依賴：Web / Validation / JPA / PostgreSQL / Flyway / Lombok
* [x] A3. 新增 `docker-compose.yml` 啟 Postgres
* [x] A4. 設定 `application.yml`（ddl-auto: validate、open-in-view: false）
* [x] A5. Flyway 建立 V1 migration（先建 debts / installments 表）
* [ ] A6. 本機啟動成功，確認 migration 有跑

**Done 定義：** `mvn spring-boot:run` 可啟動，DB 有 tables。

---

## Milestone B：資料模型與 Repository

* [ ] B1. 建立 `Debt` Entity + Repository
* [ ] B2. 建立 `DebtInstallment` Entity + Repository
* [ ] B3. 建立 enum：DebtStatus（ACTIVE / PAID_OFF）
* [ ] B4. 建立基本 DTO（CreateDebtRequest、UpdateInstallmentRequest、MarkPaidRequest）

**Done 定義：** 可從 Repository 存取資料、測試新增/查詢。

---

## Milestone C：Service（核心商業邏輯）

* [ ] C1. `DebtService.createDebt()`：建立 Debt
* [ ] C2. `ScheduleService.generateInstallments()`：依期數生成 N 筆 installments
* [ ] C3. `InstallmentService.markPaid()`：勾選已繳（含 paidAt）
* [ ] C4. `DebtService.refreshStatus()`：若全 paid → PAID_OFF
* [ ] C5. `StatsService`：計算 total outstanding / total paid

**Done 定義：** 服務層可完整跑通「新增負債 → 生成分期 → 勾選一期 → 統計更新」。

---

## Milestone D：API（前後端串接點）

* [ ] D1. Admin API：新增 Debt + 生成分期

  * `POST /api/admin/debts`
* [ ] D2. Admin API：查 Debt 列表（含彙總）

  * `GET /api/admin/debts`
* [ ] D3. Admin API：查某 Debt 分期

  * `GET /api/admin/debts/{id}/installments`
* [ ] D4. Admin API：調整某期（dueDate/amount）

  * `PUT /api/admin/installments/{id}`
* [ ] D5. Viewer API：查「要繳的清單」

  * `GET /api/installments?status=ACTIVE`
* [ ] D6. Viewer API：勾選已繳

  * `PUT /api/installments/{id}/paid`
* [ ] D7. 統計 API

  * `GET /api/stats`

**Done 定義：** Postman/curl 可完整走流程。

---

## Milestone E：前端（之後再做）

* [ ] E1. Viewer：清單 + 勾選
* [ ] E2. Admin：新增 Debt + 檢視/調整分期
* [ ] E3. UI 美化（dark fintech minimalism）






















