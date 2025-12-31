# Family Payment Tracker API

A Spring Boot REST API for managing family debt payments and installments. This application helps track payment schedules, manage debts, and monitor payment progress.

## 🚀 Features

- **Admin APIs**: Create and manage debts, adjust monthly payments
- **Dashboard APIs**: View payment statistics and visualizations
- **Payment APIs**: General payment operations
- **Swagger/OpenAPI Documentation**: Complete interactive API documentation
- **PostgreSQL Database**: Robust data storage with Flyway migrations

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use included Maven wrapper)
- Docker and Docker Compose (for PostgreSQL database)

## 🛠️ Setup Instructions

### 1. Start PostgreSQL Database

Make sure Docker is running, then start the database:

```bash
docker-compose up -d
```

This will start PostgreSQL on port 5432 with:
- Database: `family_payment`
- Username: `fpt`
- Password: `fpt`

### 2. Start the Application

Using Maven wrapper (Windows):
```bash
.\mvnw.cmd spring-boot:run
```

Using Maven wrapper (Linux/Mac):
```bash
./mvnw spring-boot:run
```

Or if you have Maven installed globally:
```bash
mvn spring-boot:run
```

### 3. Access the Application

Once the application starts, access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Landing Page**: http://localhost:8080/
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

## 📚 API Documentation

The application includes comprehensive Swagger/OpenAPI documentation:

### Admin APIs (`/api/admin`)
- Create, read, update, and delete debts
- Manage installments (monthly payments)
- Bulk update operations

### Dashboard APIs (`/api/dashboard`)
- Get payment summary and statistics
- Monthly payment breakdowns

### Payment APIs (`/api/payments`)
- Payment status and general operations

## 🏗️ Project Structure

```
famliy-payment-tracker/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/eddie/famliy_payment_tracker/
│   │   │       ├── config/          # Configuration (OpenAPI, etc.)
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── dto/            # Data Transfer Objects
│   │   │       └── FamliyPaymentTrackerApplication.java
│   │   └── resources/
│   │       ├── application.yaml    # Application configuration
│   │       ├── db/migration/       # Flyway migrations
│   │       └── static/             # Static resources (HTML, etc.)
│   └── test/                       # Test files
├── docker-compose.yml              # PostgreSQL database setup
├── pom.xml                         # Maven dependencies
└── README.md                       # This file
```

## 🗄️ Database

The application uses PostgreSQL with Flyway for database migrations:

- All migrations are in `src/main/resources/db/migration/`
- Database schema is managed through Flyway migrations
- Current migration: `V1__init.sql` (creates debts and debt_installments tables)

## 🔧 Configuration

Main configuration is in `src/main/resources/application.yaml`:

- Server port: 8080
- Database connection settings
- Flyway migration configuration
- Logging levels

## 🧪 Testing

Run tests using Maven:

```bash
.\mvnw.cmd test
```

## 📝 License

MIT License

## 👤 Author

Eddie

## 🙏 Acknowledgments

- Spring Boot
- SpringDoc OpenAPI (Swagger)
- PostgreSQL
- Flyway

---

For more detailed documentation, see the API documentation at http://localhost:8080/swagger-ui.html when the application is running.












