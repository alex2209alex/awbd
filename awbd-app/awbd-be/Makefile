# === service-specific variables ===
DOCKER_USER := vladandries
PROJECT     := awbd
TAG         := awbd-be-0.0.1
IMAGE_NAME  := $(DOCKER_USER)/$(PROJECT):$(TAG)

.PHONY: build image push

# 1. Build the project and create an executable JAR
build:
	mvn clean package spring-boot:repackage

# 2. Build the Docker image
image: build
	docker build -t $(IMAGE_NAME) .

# 3. Push it to Docker Hub
push: image
	docker push $(IMAGE_NAME)

# Optional: run locally for test
# run:
#	docker run -p 8080:8080 $(IMAGE_NAME)