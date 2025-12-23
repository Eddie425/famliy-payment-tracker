# How to Start the Application

## Step 1: Start PostgreSQL Database

Make sure Docker is running, then start the database:

```bash
docker-compose up -d
```

This will start PostgreSQL on port 5432.

## Step 2: Start Spring Boot Application

**Use the Maven Wrapper (no need to install Maven):**

On Windows (PowerShell or CMD):
```bash
.\mvnw.cmd spring-boot:run
```

Or if you prefer to compile first:
```bash
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

**Alternative:** If you have Maven installed globally:
```bash
mvn spring-boot:run
```

## Step 3: Access Swagger UI

Once the application starts (you'll see "Started FamliyPaymentTrackerApplication"), open:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## Troubleshooting

### Database Connection Error

If you see database connection errors:
1. Make sure Docker is running
2. Start the database: `docker-compose up -d`
3. Wait a few seconds for PostgreSQL to fully start
4. Check if database is running: `docker ps`

### Port Already in Use

If port 8080 is already in use:
1. Find what's using it: `netstat -ano | findstr :8080`
2. Change the port in `application.yaml`:
   ```yaml
   server:
     port: 8081
   ```
3. Then access Swagger at: http://localhost:8081/swagger-ui.html

### Maven Command Not Found

If you see "mvn: command not found", use the Maven wrapper instead:
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Or if you're in Git Bash
./mvnw spring-boot:run
```

### Application Won't Start

Check the console output for errors. Common issues:
- Database not running (start Docker Desktop first)
- Database name mismatch (should be `family_payment`)
- Missing dependencies (run `.\mvnw.cmd clean install`)










