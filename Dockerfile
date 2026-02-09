# Step 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# Using 'package' instead of 'install' is slightly faster for Docker
RUN ./mvnw clean package -DskipTests

# Step 2: Run the application
# Use Eclipse Temurin instead of the old openjdk image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]