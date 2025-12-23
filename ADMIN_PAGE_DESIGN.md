# Admin Page Design

## Overview
Admin page for managing debts and adjusting monthly payment installments. Allows admins to:
- Create new debts
- View all debts and their installments
- Adjust individual installment amounts and due dates
- Bulk update multiple installments
- View payment statistics

## API Endpoints

### 1. Create Debt
**POST** `/api/admin/debts`

Creates a new debt and automatically generates installments based on the installment count.

**Request:**
```json
{
  "title": "Car Loan",
  "totalAmount": 300000,
  "installmentCount": 12,
  "startDate": "2024-01-01",
  "interestRate": 5.5
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "title": "Car Loan",
  "totalAmount": 300000,
  "installmentCount": 12,
  "startDate": "2024-01-01",
  "interestRate": 5.5,
  "status": "ACTIVE",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

---

### 2. Get All Debts
**GET** `/api/admin/debts?status=ACTIVE&includeInstallments=true`

**Query Parameters:**
- `status` (optional): Filter by status (ACTIVE, PAID_OFF)
- `includeInstallments` (default: false): Include installment details

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Car Loan",
    "totalAmount": 300000,
    "installmentCount": 12,
    "startDate": "2024-01-01",
    "status": "ACTIVE",
    "summary": {
      "paidAmount": 90000,
      "remainingAmount": 210000,
      "paidInstallmentsCount": 3,
      "remainingInstallmentsCount": 9
    },
    "installments": [...] // if includeInstallments=true
  }
]
```

---

### 3. Get Debt by ID
**GET** `/api/admin/debts/{id}?includeInstallments=true`

**Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Car Loan",
  "totalAmount": 300000,
  "installmentCount": 12,
  "startDate": "2024-01-01",
  "status": "ACTIVE",
  "summary": {
    "paidAmount": 90000,
    "remainingAmount": 210000,
    "paidInstallmentsCount": 3,
    "remainingInstallmentsCount": 9
  },
  "installments": [
    {
      "id": 1,
      "debtId": 1,
      "debtTitle": "Car Loan",
      "installmentNumber": 1,
      "amount": 25000,
      "dueDate": "2024-01-15",
      "paid": true,
      "paidAt": "2024-01-10",
      "isOverdue": false
    },
    ...
  ]
}
```

---

### 4. Get Debt Installments
**GET** `/api/admin/debts/{id}/installments`

Returns all installments for a specific debt, sorted by installment number.

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "debtId": 1,
    "debtTitle": "Car Loan",
    "installmentNumber": 1,
    "amount": 25000,
    "dueDate": "2024-01-15",
    "paid": true,
    "paidAt": "2024-01-10",
    "isOverdue": false
  },
  {
    "id": 2,
    "debtId": 1,
    "debtTitle": "Car Loan",
    "installmentNumber": 2,
    "amount": 25000,
    "dueDate": "2024-02-15",
    "paid": true,
    "paidAt": "2024-02-12",
    "isOverdue": false
  }
]
```

---

### 5. Update Installment ⭐ (Main Feature)
**PUT** `/api/admin/installments/{id}`

**This is the main endpoint for adjusting monthly payments.**

Allows updating the amount and/or due date of a specific installment.

**Request:**
```json
{
  "amount": 30000,      // Optional - only update if provided
  "dueDate": "2024-04-20"  // Optional - only update if provided
}
```

**Response:** `200 OK`
```json
{
  "id": 4,
  "debtId": 1,
  "debtTitle": "Car Loan",
  "installmentNumber": 4,
  "amount": 30000,
  "dueDate": "2024-04-20",
  "paid": false,
  "isOverdue": false
}
```

**Example Use Cases:**
- Adjust payment amount due to interest rate change
- Reschedule payment date
- Handle payment plan modifications

---

### 6. Bulk Update Installments
**PUT** `/api/admin/installments/bulk`

Update multiple installments at once. Useful for adjusting an entire payment schedule.

**Request:**
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

**Response:** `200 OK`
```json
[
  {
    "id": 4,
    "amount": 30000,
    "dueDate": "2024-04-20",
    ...
  },
  {
    "id": 5,
    "amount": 30000,
    "dueDate": "2024-05-20",
    ...
  }
]
```

---

### 7. Delete Debt
**DELETE** `/api/admin/debts/{id}`

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Debt deleted successfully"
}
```

---

## Frontend Admin Page Design

### Page Layout

```
┌─────────────────────────────────────────────────────────┐
│  Admin Dashboard - Payment Management                    │
├─────────────────────────────────────────────────────────┤
│                                                           │
│  [Create New Debt] Button                                │
│                                                           │
│  ┌─────────────────────────────────────────────────┐   │
│  │ Debt List                                        │   │
│  ├─────────────────────────────────────────────────┤   │
│  │ Car Loan                    [View] [Edit] [Delete]│   │
│  │ $300,000 | 3/12 paid | $90,000 paid             │   │
│  ├─────────────────────────────────────────────────┤   │
│  │ Credit Card                [View] [Edit] [Delete]│   │
│  │ $200,000 | 2/10 paid | $40,000 paid            │   │
│  └─────────────────────────────────────────────────┘   │
│                                                           │
└─────────────────────────────────────────────────────────┘
```

### Debt Detail / Installment Management Page

```
┌─────────────────────────────────────────────────────────┐
│  Car Loan - Installment Management                       │
│  Total: $300,000 | Paid: $90,000 | Remaining: $210,000 │
├─────────────────────────────────────────────────────────┤
│                                                           │
│  Installment Schedule:                                    │
│                                                           │
│  ┌─────────────────────────────────────────────────┐   │
│  │ #1 | $25,000 | Due: 2024-01-15 | ✓ Paid        │   │
│  │     [View Details]                               │   │
│  ├─────────────────────────────────────────────────┤   │
│  │ #2 | $25,000 | Due: 2024-02-15 | ✓ Paid        │   │
│  │     [View Details]                               │   │
│  ├─────────────────────────────────────────────────┤   │
│  │ #3 | $25,000 | Due: 2024-03-15 | ✓ Paid        │   │
│  │     [View Details]                               │   │
│  ├─────────────────────────────────────────────────┤   │
│  │ #4 | $25,000 | Due: 2024-04-15 | ⚠ Unpaid      │   │
│  │     [Edit] [Mark Paid]                           │   │
│  ├─────────────────────────────────────────────────┤   │
│  │ #5 | $25,000 | Due: 2024-05-15 | ⚠ Unpaid      │   │
│  │     [Edit] [Mark Paid]                           │   │
│  └─────────────────────────────────────────────────┘   │
│                                                           │
│  [Bulk Edit] Button                                       │
│                                                           │
└─────────────────────────────────────────────────────────┘
```

### Edit Installment Modal/Dialog

```
┌─────────────────────────────────────┐
│  Edit Installment #4                │
├─────────────────────────────────────┤
│                                     │
│  Amount:                            │
│  [25000        ] (in cents)         │
│                                     │
│  Due Date:                          │
│  [2024-04-15] 📅                   │
│                                     │
│  Status: Unpaid                     │
│                                     │
│  [Cancel]  [Save Changes]           │
│                                     │
└─────────────────────────────────────┘
```

### Bulk Edit Modal

```
┌─────────────────────────────────────┐
│  Bulk Edit Installments              │
├─────────────────────────────────────┤
│                                     │
│  Select Installments:               │
│  ☑ #4 - $25,000 - 2024-04-15       │
│  ☑ #5 - $25,000 - 2024-05-15       │
│  ☐ #6 - $25,000 - 2024-06-15       │
│                                     │
│  Adjust Amount:                     │
│  [Apply to all: +5000] or          │
│  [Set all to: 30000]                │
│                                     │
│  Adjust Due Date:                   │
│  [Shift all by: +5 days] or         │
│  [Set all to: 2024-04-20]           │
│                                     │
│  [Cancel]  [Apply Changes]           │
│                                     │
└─────────────────────────────────────┘
```

## Frontend Implementation Examples

### React Component Example

```jsx
function AdminDebtManagement() {
  const [debts, setDebts] = useState([]);
  const [selectedDebt, setSelectedDebt] = useState(null);
  
  // Fetch debts
  useEffect(() => {
    fetch('/api/admin/debts?includeInstallments=true')
      .then(res => res.json())
      .then(setDebts);
  }, []);
  
  // Update installment
  const updateInstallment = async (installmentId, updates) => {
    const response = await fetch(`/api/admin/installments/${installmentId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updates)
    });
    const updated = await response.json();
    // Refresh debt data
    refreshDebt(updated.debtId);
  };
  
  return (
    <div>
      <h1>Debt Management</h1>
      <button onClick={createNewDebt}>Create New Debt</button>
      
      <DebtList debts={debts} onSelect={setSelectedDebt} />
      
      {selectedDebt && (
        <InstallmentTable 
          installments={selectedDebt.installments}
          onUpdate={updateInstallment}
        />
      )}
    </div>
  );
}

function InstallmentTable({ installments, onUpdate }) {
  const [editing, setEditing] = useState(null);
  
  return (
    <table>
      <thead>
        <tr>
          <th>#</th>
          <th>Amount</th>
          <th>Due Date</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {installments.map(inst => (
          <tr key={inst.id}>
            <td>{inst.installmentNumber}</td>
            <td>${inst.amount / 100}</td>
            <td>{inst.dueDate}</td>
            <td>{inst.paid ? '✓ Paid' : '⚠ Unpaid'}</td>
            <td>
              {!inst.paid && (
                <button onClick={() => setEditing(inst)}>Edit</button>
              )}
            </td>
          </tr>
        ))}
      </tbody>
      
      {editing && (
        <EditInstallmentModal
          installment={editing}
          onSave={(updates) => {
            onUpdate(editing.id, updates);
            setEditing(null);
          }}
          onCancel={() => setEditing(null)}
        />
      )}
    </table>
  );
}
```

## Usage Examples

### Adjust Single Payment

```bash
# Change installment #4 amount to $30,000
curl -X PUT http://localhost:8080/api/admin/installments/4 \
  -H "Content-Type: application/json" \
  -d '{"amount": 3000000}'

# Change installment #4 due date
curl -X PUT http://localhost:8080/api/admin/installments/4 \
  -H "Content-Type: application/json" \
  -d '{"dueDate": "2024-04-20"}'

# Change both amount and due date
curl -X PUT http://localhost:8080/api/admin/installments/4 \
  -H "Content-Type: application/json" \
  -d '{"amount": 3000000, "dueDate": "2024-04-20"}'
```

### Bulk Adjust Payments

```bash
# Adjust multiple installments at once
curl -X PUT http://localhost:8080/api/admin/installments/bulk \
  -H "Content-Type: application/json" \
  -d '[
    {"installmentId": 4, "amount": 3000000, "dueDate": "2024-04-20"},
    {"installmentId": 5, "amount": 3000000, "dueDate": "2024-05-20"}
  ]'
```

## Implementation Checklist

- [x] Create DTOs (CreateDebtRequest, UpdateInstallmentRequest, etc.)
- [x] Create AdminController with all endpoints
- [ ] Implement DebtService.createDebt()
- [ ] Implement InstallmentService.updateInstallment()
- [ ] Implement InstallmentService.bulkUpdateInstallments()
- [ ] Add validation for installment updates
- [ ] Add audit logging for changes
- [ ] Frontend: Create admin page UI
- [ ] Frontend: Implement installment editing form
- [ ] Frontend: Implement bulk edit functionality

## Notes

- All amounts are stored in the smallest currency unit (e.g., cents)
- Only unpaid installments should be editable (or allow editing paid ones with warning)
- Consider adding validation to prevent adjusting installments that are already paid
- Future: Add change history/audit trail for installment modifications











