# API Documentation Summary

## Swagger UI Access

Once the application is running, access Swagger UI at:
- **http://localhost:8080/swagger-ui.html**
- **http://localhost:8080/swagger-ui/index.html**

## API Endpoints Overview

### 🔧 Admin APIs (`/api/admin`)

#### 1. Create Debt
- **POST** `/api/admin/debts`
- **Description**: Creates a new debt and automatically generates installments
- **Request Body**:
  ```json
  {
    "title": "Car Loan",
    "totalAmount": 300000,
    "installmentCount": 12,
    "startDate": "2024-01-01",
    "interestRate": 5.5
  }
  ```
- **Response**: `201 Created` - DebtResponseDTO

#### 2. Get All Debts
- **GET** `/api/admin/debts?status=ACTIVE&includeInstallments=true`
- **Description**: Retrieves all debts with optional filtering
- **Query Parameters**:
  - `status` (optional): ACTIVE or PAID_OFF
  - `includeInstallments` (default: false): Include installment details
- **Response**: `200 OK` - List<DebtResponseDTO>

#### 3. Get Debt by ID
- **GET** `/api/admin/debts/{id}?includeInstallments=true`
- **Description**: Retrieves detailed information about a specific debt
- **Path Parameters**:
  - `id`: Debt ID
- **Query Parameters**:
  - `includeInstallments` (default: true): Include installment details
- **Response**: `200 OK` - DebtResponseDTO

#### 4. Get Debt Installments
- **GET** `/api/admin/debts/{id}/installments`
- **Description**: Retrieves all installments for a specific debt
- **Path Parameters**:
  - `id`: Debt ID
- **Response**: `200 OK` - List<InstallmentResponseDTO>

#### 5. ⭐ Update Installment (Adjust Monthly Payment)
- **PUT** `/api/admin/installments/{id}`
- **Description**: Updates the amount and/or due date of a specific installment
- **Path Parameters**:
  - `id`: Installment ID
- **Request Body**:
  ```json
  {
    "amount": 30000,        // Optional
    "dueDate": "2024-04-20" // Optional
  }
  ```
- **Response**: `200 OK` - InstallmentResponseDTO

#### 6. Bulk Update Installments
- **PUT** `/api/admin/installments/bulk`
- **Description**: Updates multiple installments in a single request
- **Request Body**:
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
- **Response**: `200 OK` - List<InstallmentResponseDTO>

#### 7. Delete Debt
- **DELETE** `/api/admin/debts/{id}`
- **Description**: Deletes a debt and all its associated installments
- **Path Parameters**:
  - `id`: Debt ID
- **Response**: `200 OK` - ApiResponse

---

### 📊 Dashboard APIs (`/api/dashboard`)

#### 1. Get Dashboard Summary
- **GET** `/api/dashboard/summary?year=2024&month=1`
- **Description**: Returns comprehensive payment statistics including totals, monthly breakdown, and visualization data
- **Query Parameters**:
  - `year` (optional): Filter by year
  - `month` (optional): Filter by month (1-12)
- **Response**: `200 OK` - DashboardSummaryDTO
  - Includes: summary info, monthly breakdown, debt breakdown, visualization data

#### 2. Get Monthly Breakdown
- **GET** `/api/dashboard/monthly?year=2024&month=1`
- **Description**: Returns detailed payment information for a specific month
- **Query Parameters**:
  - `year` (required): Year (e.g., 2024)
  - `month` (required): Month (1-12)
- **Response**: `200 OK` - MonthlyBreakdownDTO

---

### 💳 Payment APIs (`/api/payments`)

#### 1. Get Payment Status
- **GET** `/api/payments`
- **Description**: Returns a simple status message indicating the API is operational
- **Response**: `200 OK` - Map with message and status

---

## Data Transfer Objects (DTOs)

### CreateDebtRequest
```json
{
  "title": "string (required, max 200 chars)",
  "totalAmount": "long (required, positive)",
  "installmentCount": "integer (required, min 1)",
  "startDate": "date (required, ISO format)",
  "interestRate": "decimal (optional, >= 0)"
}
```

### UpdateInstallmentRequest
```json
{
  "amount": "long (optional, positive)",
  "dueDate": "date (optional, ISO format)"
}
```

### DebtResponseDTO
```json
{
  "id": "long",
  "title": "string",
  "totalAmount": "long",
  "installmentCount": "integer",
  "startDate": "date",
  "interestRate": "decimal",
  "status": "string (ACTIVE or PAID_OFF)",
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "installments": "List<InstallmentResponseDTO> (optional)",
  "summary": {
    "paidAmount": "long",
    "remainingAmount": "long",
    "paidInstallmentsCount": "integer",
    "remainingInstallmentsCount": "integer"
  }
}
```

### InstallmentResponseDTO
```json
{
  "id": "long",
  "debtId": "long",
  "debtTitle": "string",
  "installmentNumber": "integer",
  "amount": "long",
  "dueDate": "date",
  "paid": "boolean",
  "paidAt": "date (nullable)",
  "isOverdue": "boolean",
  "createdAt": "date",
  "updatedAt": "date"
}
```

### DashboardSummaryDTO
```json
{
  "summary": {
    "totalPaid": "long",
    "totalOutstanding": "long",
    "totalAmount": "long",
    "progressPercentage": "decimal",
    "activeDebtsCount": "integer",
    "completedDebtsCount": "integer"
  },
  "monthlyBreakdown": [
    {
      "month": "string (YYYY-MM)",
      "monthLabel": "string",
      "totalDue": "long",
      "totalPaid": "long",
      "remaining": "long",
      "isComplete": "boolean",
      "installments": [...]
    }
  ],
  "debtBreakdown": [...],
  "visualizationData": {
    "chartData": {
      "labels": ["string"],
      "values": ["long"],
      "colors": ["string"]
    },
    "progressBarData": {
      "current": "long",
      "total": "long",
      "percentage": "decimal"
    }
  }
}
```

---

## How to View in Swagger UI

1. **Start the application**:
   ```bash
   mvn spring-boot:run
   ```

2. **Open your browser** and navigate to:
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **Explore the APIs**:
   - Expand any endpoint to see details
   - Click "Try it out" to test endpoints
   - View request/response schemas
   - See example values

## Quick Test Examples

### Test Create Debt
```bash
curl -X POST http://localhost:8080/api/admin/debts \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Car Loan",
    "totalAmount": 300000,
    "installmentCount": 12,
    "startDate": "2024-01-01",
    "interestRate": 5.5
  }'
```

### Test Update Installment
```bash
curl -X PUT http://localhost:8080/api/admin/installments/4 \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 30000,
    "dueDate": "2024-04-20"
  }'
```

### Test Dashboard Summary
```bash
curl http://localhost:8080/api/dashboard/summary
```

---

## Notes

- All amounts are in the smallest currency unit (e.g., cents for USD)
- Dates are in ISO format: `YYYY-MM-DD`
- The Swagger UI provides interactive testing of all endpoints
- All endpoints are currently accessible without authentication (future versions will add RBAC)










