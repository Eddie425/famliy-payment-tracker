# Quick Start Guide

## Prerequisites
1. **Docker Desktop** must be running
2. No need to install Maven - the project includes Maven Wrapper

## Step-by-Step

### 1. Start Docker Desktop
- Open Docker Desktop application
- Wait until it shows "Docker Desktop is running"

### 2. Start PostgreSQL Database
Open PowerShell or CMD in the project directory and run:
```bash
docker-compose up -d
```

Wait a few seconds, then verify it's running:
```bash
docker ps
```

You should see `family-payment-postgres` container running.

### 3. Start Spring Boot Application
In the same terminal, run:
```bash
.\mvnw.cmd spring-boot:run
```

**Note:** Use `.\mvnw.cmd` (Maven Wrapper) - you don't need to install Maven!

### 4. Access Swagger UI
Once you see `Started FamliyPaymentTrackerApplication` in the console, open your browser:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## Troubleshooting

### "mvn: command not found"
✅ **Solution:** Use `.\mvnw.cmd` instead of `mvn`

### "Docker daemon is not running"
✅ **Solution:** 
1. Open Docker Desktop
2. Wait for it to fully start
3. Try `docker-compose up -d` again

### "Connection refused" when accessing Swagger
✅ **Solution:**
1. Make sure the application is running (check terminal for "Started" message)
2. Wait a few more seconds after seeing "Started"
3. Try refreshing the browser

### Database connection error
✅ **Solution:**
1. Check Docker is running: `docker ps`
2. Check database container: `docker ps | findstr postgres`
3. If not running, start it: `docker-compose up -d`
4. Wait 10-15 seconds for PostgreSQL to fully initialize

## All-in-One Commands

If everything is set up, you can run these commands in sequence:

```bash
# 1. Start database
docker-compose up -d

# 2. Wait 5 seconds, then start application
.\mvnw.cmd spring-boot:run
```

## What You'll See

When the application starts successfully, you'll see:
```
...
Started FamliyPaymentTrackerApplication in X.XXX seconds
```

Then you can access Swagger UI at: http://localhost:8080/swagger-ui.html














