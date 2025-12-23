# Dashboard Implementation Guide

## Quick Start

This guide explains how to implement the payment dashboard feature that displays:
- **How much has been paid** (total paid amount)
- **How much remains to be paid** (total outstanding)
- **Visual representations** (charts, progress bars)

## Architecture Overview

```
┌─────────────────┐
│  Frontend       │  (Future - React/Vue/etc.)
│  - Charts       │
│  - Progress Bars│
└────────┬────────┘
         │ HTTP GET
         ▼
┌─────────────────┐
│ DashboardController│  ← You are here
│ /api/dashboard  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ DashboardService│  ← Need to implement
│ - Aggregate data│
│ - Calculate stats│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Repositories    │  ← Need to implement
│ - DebtRepo      │
│ - InstallmentRepo│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ PostgreSQL      │
│ - debts         │
│ - installments  │
└─────────────────┘
```

## Implementation Steps

### Step 1: Create Entities (Milestone B)
You need to create:
- `Debt` entity
- `DebtInstallment` entity
- `DebtStatus` enum

### Step 2: Create Repositories (Milestone B)
- `DebtRepository` extends `JpaRepository<Debt, Long>`
- `DebtInstallmentRepository` extends `JpaRepository<DebtInstallment, Long>`

### Step 3: Create DashboardService (New)

Create `src/main/java/com/eddie/famliy_payment_tracker/service/DashboardService.java`:

```java
@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final DebtRepository debtRepository;
    private final DebtInstallmentRepository installmentRepository;
    
    public DashboardSummaryDTO getDashboardSummary(Integer year, Integer month) {
        // 1. Get all active debts
        List<Debt> activeDebts = debtRepository.findByStatus(DebtStatus.ACTIVE);
        
        // 2. Calculate totals
        Long totalPaid = installmentRepository.sumAmountByPaid(true);
        Long totalOutstanding = installmentRepository.sumAmountByPaid(false);
        
        // 3. Get monthly breakdown
        List<MonthlyBreakdownDTO> monthly = getMonthlyBreakdown(year);
        
        // 4. Get debt breakdown
        List<DebtBreakdownDTO> debts = getDebtBreakdown(activeDebts);
        
        // 5. Build visualization data
        VisualizationDataDTO vizData = buildVisualizationData(totalPaid, totalOutstanding);
        
        // 6. Build and return DTO
        return DashboardSummaryDTO.builder()
            .summary(buildSummaryInfo(totalPaid, totalOutstanding, activeDebts))
            .monthlyBreakdown(monthly)
            .debtBreakdown(debts)
            .visualizationData(vizData)
            .build();
    }
    
    // Helper methods...
}
```

### Step 4: Add Repository Methods

In `DebtInstallmentRepository`, add:

```java
@Query("SELECT COALESCE(SUM(i.amount), 0) FROM DebtInstallment i " +
       "WHERE i.paid = :paid AND i.debt.status = 'ACTIVE'")
Long sumAmountByPaid(@Param("paid") boolean paid);

@Query("SELECT i FROM DebtInstallment i " +
       "WHERE YEAR(i.dueDate) = :year " +
       "AND MONTH(i.dueDate) = :month " +
       "AND i.debt.status = 'ACTIVE' " +
       "ORDER BY i.dueDate")
List<DebtInstallment> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
```

### Step 5: Update Controller

Replace the sample data methods in `DashboardController` with:

```java
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary(year, month);
        return ResponseEntity.ok(summary);
    }
}
```

## Frontend Visualization Examples

### Option 1: Simple Progress Bar (HTML/CSS)

```html
<div class="progress-container">
  <div class="progress-bar">
    <div class="progress-fill" style="width: 30%"></div>
  </div>
  <div class="progress-text">
    $150,000 / $500,000 (30%)
  </div>
</div>

<style>
.progress-bar {
  width: 100%;
  height: 30px;
  background-color: #ef4444;
  border-radius: 15px;
  overflow: hidden;
}
.progress-fill {
  height: 100%;
  background-color: #10b981;
  transition: width 0.3s ease;
}
</style>
```

### Option 2: Pie Chart (Chart.js)

```javascript
// Fetch data from API
const response = await fetch('/api/dashboard/summary');
const data = await response.json();

// Create pie chart
const ctx = document.getElementById('paymentChart').getContext('2d');
new Chart(ctx, {
  type: 'pie',
  data: {
    labels: data.visualizationData.chartData.labels,
    datasets: [{
      data: data.visualizationData.chartData.values,
      backgroundColor: data.visualizationData.chartData.colors
    }]
  }
});
```

### Option 3: Monthly Timeline (React Example)

```jsx
function MonthlyTimeline({ monthlyBreakdown }) {
  return (
    <div className="timeline">
      {monthlyBreakdown.map(month => (
        <div key={month.month} className="month-card">
          <h3>{month.monthLabel}</h3>
          <div className="amounts">
            <span className="paid">Paid: ${month.totalPaid.toLocaleString()}</span>
            <span className="remaining">Remaining: ${month.remaining.toLocaleString()}</span>
          </div>
          <div className="progress">
            <div 
              className="progress-bar"
              style={{ width: `${(month.totalPaid / month.totalDue) * 100}%` }}
            />
          </div>
          {month.isComplete && <span className="badge">✓ Complete</span>}
        </div>
      ))}
    </div>
  );
}
```

## API Usage Examples

### Get Full Dashboard Summary
```bash
curl http://localhost:8080/api/dashboard/summary
```

### Get Specific Month
```bash
curl http://localhost:8080/api/dashboard/monthly?year=2024&month=1
```

### Response Structure
The API returns a comprehensive JSON structure with:
- **summary**: Overall totals and counts
- **monthlyBreakdown**: Month-by-month payment details
- **debtBreakdown**: Individual debt progress
- **visualizationData**: Pre-formatted data for charts

## Next Steps

1. ✅ **Done**: Created DTOs and Controller structure
2. ⏳ **Next**: Implement Entities (Debt, DebtInstallment)
3. ⏳ **Next**: Implement Repositories
4. ⏳ **Next**: Implement DashboardService with real queries
5. ⏳ **Future**: Build frontend to consume the API

## Testing

Once implemented, test with:

```bash
# Start the application
mvn spring-boot:run

# Test the endpoint
curl http://localhost:8080/api/dashboard/summary | jq
```

## Notes

- The current `DashboardController` has sample data to show the structure
- Replace sample methods with actual service calls once entities are created
- The DTO structure is designed to be frontend-friendly
- All amounts are in the smallest currency unit (e.g., cents for USD)











