# Use an official Java runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the local system to the container
COPY out/artifacts/Book-Management-API.jar /app/Book_Management_API_jar/Book-Management-API.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "Book-Management-API.jar"]
