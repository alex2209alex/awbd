FROM eclipse-temurin:21-jdk-alpine

# Install git (needed for Spring Cloud Config Server to fetch from Git repo)
RUN apk add --no-cache git
RUN apk add --no-cache curl

# Allow override of the built JAR via Maven’s target folder
ARG JAR_FILE=target/*.jar

# Copy the JAR into the image
COPY ${JAR_FILE} app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]