FROM maven:3.8.5-openjdk-17 AS build

# Copy your project files
COPY . .

# Package the application
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim

# Copy the built JAR file from the correct path
COPY --from=build /target/Book-Management-API-0.0.1-SNAPSHOT.jar Book-Management-API.jar

# Expose the port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/Book-Management-API.jar"]
