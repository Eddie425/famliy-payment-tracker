Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting Family Payment Tracker" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Docker is running
Write-Host "[1/3] Checking Docker..." -ForegroundColor Yellow
try {
    docker ps | Out-Null
    Write-Host "✓ Docker is running" -ForegroundColor Green
} catch {
    Write-Host "✗ Docker is not running!" -ForegroundColor Red
    Write-Host "Please start Docker Desktop first, then run this script again." -ForegroundColor Yellow
    Write-Host ""
    Start-Process "https://www.docker.com/products/docker-desktop"
    exit 1
}

# Start PostgreSQL
Write-Host ""
Write-Host "[2/3] Starting PostgreSQL Database..." -ForegroundColor Yellow
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptPath
docker-compose up -d
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Failed to start database" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Database started" -ForegroundColor Green
Start-Sleep -Seconds 5

# Start Backend
Write-Host ""
Write-Host "[3/4] Starting Backend (Spring Boot)..." -ForegroundColor Yellow
$backendPath = $scriptPath
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$backendPath'; .\mvnw.cmd spring-boot:run" -WindowStyle Normal
Write-Host "✓ Backend starting..." -ForegroundColor Green

# Start Frontend
Write-Host ""
Write-Host "[4/4] Starting Frontend (React)..." -ForegroundColor Yellow
$frontendPath = Join-Path (Split-Path -Parent $scriptPath) "famliy-payment-tracker-web"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$frontendPath'; npm run dev" -WindowStyle Normal
Write-Host "✓ Frontend starting..." -ForegroundColor Green

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Services are starting..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Backend:  http://localhost:8080" -ForegroundColor White
Write-Host "Frontend: http://localhost:3000" -ForegroundColor White
Write-Host "Swagger:  http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host ""
Write-Host "Please wait 30-60 seconds for all services to fully start." -ForegroundColor Yellow
Write-Host "Then open http://localhost:3000 in your browser." -ForegroundColor Yellow
Write-Host ""
Write-Host "Opening frontend in browser..." -ForegroundColor Green
Start-Sleep -Seconds 35
Start-Process "http://localhost:3000"






