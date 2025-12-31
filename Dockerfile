# Use OpenJDK 17 as base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Run the application
# PORT will be set by the deployment platform (Railway/Render)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/famliy-payment-tracker-0.0.1-SNAPSHOT.jar"]
