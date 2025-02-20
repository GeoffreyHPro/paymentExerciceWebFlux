# Build application
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy files needed
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

# Build application JAR
RUN mvn package -DskipTests

# Create .exe
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy JAR in .exe file
COPY --from=build /app/target/*.jar app.jar

# PORT of application
EXPOSE 8080

# Command to execute application
ENTRYPOINT ["java", "-jar", "app.jar"]