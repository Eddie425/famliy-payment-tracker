@echo off
echo ========================================
echo Starting Family Payment Tracker
echo ========================================
echo.

echo [1/3] Starting PostgreSQL Database...
docker-compose up -d
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker is not running. Please start Docker Desktop first.
    pause
    exit /b 1
)

echo Waiting for database to be ready...
timeout /t 5 /nobreak >nul

echo.
echo [2/3] Starting Backend (Spring Boot)...
start "Backend - Spring Boot" cmd /k "cd /d %~dp0 && .\mvnw.cmd spring-boot:run"

echo.
echo [3/3] Starting Frontend (React)...
start "Frontend - React" cmd /k "cd /d %~dp0\..\famliy-payment-tracker-web && npm run dev"

echo.
echo ========================================
echo Services are starting...
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:3000
echo Swagger:  http://localhost:8080/swagger-ui.html
echo.
echo Please wait 30-60 seconds for all services to fully start.
echo Then open http://localhost:3000 in your browser.
echo.
pause
