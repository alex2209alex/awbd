# Use appropriate Java version (21 in your case)
FROM eclipse-temurin:21-jdk-alpine

# Argument to pick up the jar file
ARG JAR_FILE=target/*.jar

# Copy the jar into the container
COPY ${JAR_FILE} app.jar

# Expose backend port (default: 8080)
#EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java", "-jar", "/app.jar"]