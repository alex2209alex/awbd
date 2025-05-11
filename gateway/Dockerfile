# config-server/Dockerfile  (same for eureka-server, backend, …)
FROM eclipse-temurin:21-jdk-alpine

# Allow override of the built JAR via Maven’s target folder
ARG JAR_FILE=target/*.jar

# Copy that JAR into the image
COPY ${JAR_FILE} app.jar

# Run it
ENTRYPOINT ["java", "-jar", "/app.jar"]