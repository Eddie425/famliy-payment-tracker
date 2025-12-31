# Payment Dashboard Design

## Overview
This document outlines the design for displaying monthly payment information showing:
- How much has been paid
- How much remains to be paid
- Visual representations (charts, progress bars)

## API Design

### Endpoint: `GET /api/dashboard/summary`

Returns comprehensive payment statistics suitable for dashboard visualization.

#### Response Structure

```json
{
  "summary": {
    "totalPaid": 150000,
    "totalOutstanding": 350000,
    "totalAmount": 500000,
    "progressPercentage": 30.0,
    "activeDebtsCount": 3,
    "completedDebtsCount": 1
  },
  "monthlyBreakdown": [
    {
      "month": "2024-01",
      "monthLabel": "January 2024",
      "totalDue": 50000,
      "totalPaid": 50000,
      "remaining": 0,
      "isComplete": true,
      "installments": [
        {
          "debtTitle": "Car Loan",
          "amount": 30000,
          "dueDate": "2024-01-15",
          "paid": true,
          "paidAt": "2024-01-10"
        },
        {
          "debtTitle": "Credit Card",
          "amount": 20000,
          "dueDate": "2024-01-20",
          "paid": true,
          "paidAt": "2024-01-18"
        }
      ]
    },
    {
      "month": "2024-02",
      "monthLabel": "February 2024",
      "totalDue": 50000,
      "totalPaid": 30000,
      "remaining": 20000,
      "isComplete": false,
      "installments": [...]
    }
  ],
  "debtBreakdown": [
    {
      "debtId": 1,
      "title": "Car Loan",
      "totalAmount": 300000,
      "paidAmount": 90000,
      "remainingAmount": 210000,
      "progressPercentage": 30.0,
      "status": "ACTIVE"
    }
  ],
  "visualizationData": {
    "chartData": {
      "labels": ["Paid", "Remaining"],
      "values": [150000, 350000],
      "colors": ["#10b981", "#ef4444"]
    },
    "progressBarData": {
      "current": 150000,
      "total": 500000,
      "percentage": 30.0
    }
  }
}
```

## Visualization Approaches

### 1. Progress Bar (Recommended for Quick Overview)
- **Simple horizontal progress bar**
- Shows: `[████████░░░░░░░░░░░░] 30%`
- Green for paid portion, red/gray for remaining
- Easy to understand at a glance

### 2. Pie/Donut Chart
- **Two segments**: Paid vs Remaining
- Visual representation of proportions
- Can use libraries like:
  - Chart.js (frontend)
  - Apache ECharts
  - Recharts (React)

### 3. Monthly Timeline View
- **Calendar-style layout**
- Each month shows:
  - Total due
  - Paid amount (green checkmark)
  - Remaining (red indicator)
- Helps track monthly progress

### 4. Debt-by-Debt Breakdown
- **Card-based layout**
- Each debt shows:
  - Title
  - Progress bar
  - Paid/Remaining amounts
- Grouped by status (Active/Paid Off)

### 5. Combined Dashboard View
- **Top section**: Overall summary with large progress bar
- **Middle section**: Monthly breakdown (timeline or table)
- **Bottom section**: Individual debt cards

## Implementation Recommendations

### Backend (Spring Boot)
1. Create `DashboardService` to aggregate data
2. Query installments grouped by month
3. Calculate totals and percentages
4. Return structured DTOs

### Frontend (Future)
1. Use a charting library (Chart.js, Recharts, etc.)
2. Create reusable components:
   - `ProgressBar` component
   - `MonthlyCard` component
   - `DebtCard` component
3. Responsive design for mobile viewing

## Example Frontend Visualization Code (Pseudo)

```javascript
// Using Chart.js for pie chart
const chartData = {
  labels: ['Paid', 'Remaining'],
  datasets: [{
    data: [summary.totalPaid, summary.totalOutstanding],
    backgroundColor: ['#10b981', '#ef4444']
  }]
};

// Progress bar
const progress = (summary.totalPaid / summary.totalAmount) * 100;
<div className="progress-bar">
  <div 
    className="progress-fill" 
    style={{ width: `${progress}%` }}
  />
</div>
```

## API Endpoints Needed

1. **`GET /api/dashboard/summary`** - Main dashboard data
2. **`GET /api/dashboard/monthly?year=2024&month=1`** - Specific month details
3. **`GET /api/dashboard/debts`** - All debts with progress

## Performance Considerations

- Cache summary data (refresh every 5 minutes)
- Use database aggregation queries (SUM, COUNT)
- Index on `paid`, `due_date`, `status` columns
- Consider pagination for monthly breakdown if many months

















