# Stage 1: Build the JAR file (using Java 17)
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the entire project (parent pom + modules)
COPY pom.xml .
COPY data/pom.xml data/
COPY core/pom.xml core/
COPY web/pom.xml web/

# Download dependencies (cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy source code
COPY data/src data/src
COPY core/src core/src
COPY web/src web/src

# Build the web module (produces executable JAR)
RUN mvn clean package -pl web -am -DskipTests

# Stage 2: Runtime image (Java 17 JRE)
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/web/target/*.jar app.jar

# Expose the port your Spring Boot app listens on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]