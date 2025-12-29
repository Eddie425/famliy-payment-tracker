# Admin API Quick Reference

## Overview
Complete API endpoints for admin to manage debts and adjust monthly payments.

## Core Endpoints

### 1. Create Debt
```http
POST /api/admin/debts
Content-Type: application/json

{
  "title": "Car Loan",
  "totalAmount": 300000,
  "installmentCount": 12,
  "startDate": "2024-01-01",
  "interestRate": 5.5
}
```

### 2. List All Debts
```http
GET /api/admin/debts?status=ACTIVE&includeInstallments=true
```

### 3. Get Debt Details
```http
GET /api/admin/debts/{id}?includeInstallments=true
```

### 4. Get Debt Installments
```http
GET /api/admin/debts/{id}/installments
```

### 5. ⭐ Update Installment (Adjust Monthly Payment)
```http
PUT /api/admin/installments/{id}
Content-Type: application/json

{
  "amount": 30000,        // Optional
  "dueDate": "2024-04-20" // Optional
}
```

### 6. Bulk Update Installments
```http
PUT /api/admin/installments/bulk
Content-Type: application/json

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

### 7. Delete Debt
```http
DELETE /api/admin/debts/{id}
```

## Common Use Cases

### Adjust Payment Amount
```bash
curl -X PUT http://localhost:8080/api/admin/installments/4 \
  -H "Content-Type: application/json" \
  -d '{"amount": 3000000}'
```

### Reschedule Payment Date
```bash
curl -X PUT http://localhost:8080/api/admin/installments/4 \
  -H "Content-Type: application/json" \
  -d '{"dueDate": "2024-04-20"}'
```

### Adjust Both Amount and Date
```bash
curl -X PUT http://localhost:8080/api/admin/installments/4 \
  -H "Content-Type: application/json" \
  -d '{"amount": 3000000, "dueDate": "2024-04-20"}'
```

### View All Installments for a Debt
```bash
curl http://localhost:8080/api/admin/debts/1/installments
```

### Bulk Adjust Multiple Payments
```bash
curl -X PUT http://localhost:8080/api/admin/installments/bulk \
  -H "Content-Type: application/json" \
  -d '[
    {"installmentId": 4, "amount": 3000000},
    {"installmentId": 5, "amount": 3000000}
  ]'
```

## Response Examples

### Installment Response
```json
{
  "id": 4,
  "debtId": 1,
  "debtTitle": "Car Loan",
  "installmentNumber": 4,
  "amount": 30000,
  "dueDate": "2024-04-20",
  "paid": false,
  "paidAt": null,
  "isOverdue": false
}
```

### Debt with Summary
```json
{
  "id": 1,
  "title": "Car Loan",
  "totalAmount": 300000,
  "installmentCount": 12,
  "status": "ACTIVE",
  "summary": {
    "paidAmount": 90000,
    "remainingAmount": 210000,
    "paidInstallmentsCount": 3,
    "remainingInstallmentsCount": 9
  }
}
```

## Notes

- All amounts are in smallest currency unit (e.g., cents)
- Dates are in ISO format: `YYYY-MM-DD`
- `amount` and `dueDate` in UpdateInstallmentRequest are optional - only provided fields will be updated
- Installments can be updated even if already paid (use with caution)















