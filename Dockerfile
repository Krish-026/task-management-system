# Step 1: Build the JAR using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the JAR using Java Runtime
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /target/task-system-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Clean way to run Spring Boot with Render Environment Variables
CMD ["java", "-jar", "app.jar"]
