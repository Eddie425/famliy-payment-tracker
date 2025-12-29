# Swagger/OpenAPI Documentation Setup

## Overview
This project uses SpringDoc OpenAPI 3 (Swagger) for API documentation. The documentation is automatically generated from your controllers and DTOs.

## Accessing Swagger UI

Once the application is running, you can access:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

## Features

### API Documentation Includes:
- ✅ All endpoints with descriptions
- ✅ Request/Response schemas
- ✅ Parameter descriptions
- ✅ Example requests and responses
- ✅ Try-it-out functionality (test APIs directly from Swagger UI)

### Organized by Tags:
- **Admin APIs**: Debt and installment management
- **Dashboard APIs**: Payment statistics and visualizations
- **Payment APIs**: General payment endpoints

## Configuration

The Swagger configuration is in:
- `src/main/java/com/eddie/famliy_payment_tracker/config/OpenApiConfig.java`

You can customize:
- API title and description
- Contact information
- Server URLs
- License information

## Customizing Documentation

### Adding Descriptions to Endpoints

Use `@Operation` annotation:
```java
@Operation(
    summary = "Short description",
    description = "Detailed description of what this endpoint does"
)
```

### Documenting Parameters

Use `@Parameter` annotation:
```java
@Parameter(description = "Description of the parameter")
@RequestParam String status
```

### Documenting Responses

Use `@ApiResponse` annotation:
```java
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "404", description = "Not found")
})
```

### Adding Tags

Use `@Tag` annotation on controllers:
```java
@Tag(name = "Admin APIs", description = "Description of this API group")
```

## Dependencies

The following dependency is added to `pom.xml`:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

## Testing APIs from Swagger UI

1. Start the application: `mvn spring-boot:run`
2. Open http://localhost:8080/swagger-ui.html
3. Expand any endpoint
4. Click "Try it out"
5. Fill in parameters (if any)
6. Click "Execute"
7. View the response

## Example Usage

### Testing Create Debt Endpoint

1. Navigate to **Admin APIs** → **POST /api/admin/debts**
2. Click "Try it out"
3. Enter request body:
```json
{
  "title": "Car Loan",
  "totalAmount": 300000,
  "installmentCount": 12,
  "startDate": "2024-01-01",
  "interestRate": 5.5
}
```
4. Click "Execute"
5. View the response

### Testing Update Installment

1. Navigate to **Admin APIs** → **PUT /api/admin/installments/{id}**
2. Click "Try it out"
3. Enter installment ID: `4`
4. Enter request body:
```json
{
  "amount": 30000,
  "dueDate": "2024-04-20"
}
```
5. Click "Execute"

## Disabling Swagger in Production

To disable Swagger in production, add to `application.yaml`:

```yaml
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false
```

Or use profiles:
```yaml
spring:
  profiles:
    active: prod

---
spring:
  config:
    activate:
      on-profile: prod
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false
```

## Troubleshooting

### Swagger UI not loading
- Check if the application is running on port 8080
- Verify the dependency is in `pom.xml`
- Check for any compilation errors

### Endpoints not showing
- Ensure controllers have `@RestController` annotation
- Check that request mappings are correct
- Verify no errors in application logs

### Schema not displaying correctly
- Ensure DTOs have proper getters/setters (Lombok should handle this)
- Check that Jackson is properly configured (included in spring-boot-starter-web)

## Additional Resources

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)














