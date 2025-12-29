package com.eddie.famliy_payment_tracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration
 * Access Swagger UI at: http://localhost:8080/swagger-ui.html
 * Access API docs at: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI familyPaymentTrackerOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development server");

        Contact contact = new Contact();
        contact.setEmail("admin@example.com");
        contact.setName("Family Payment Tracker API Support");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Family Payment Tracker API")
                .version("1.0.0")
                .contact(contact)
                .description("""
                        API for managing family debt payments and installments.
                        
                        ## Features
                        - **Admin APIs**: Create and manage debts, adjust monthly payments
                        - **Viewer APIs**: View payment schedules and mark payments as paid
                        - **Dashboard APIs**: Get payment statistics and visualizations
                        
                        ## Authentication
                        Currently no authentication is required. All endpoints are accessible.
                        Future versions will add role-based access control.
                        """)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}














