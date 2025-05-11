# === service-specific variables ===
DOCKER_USER := vladandries
PROJECT     := awbd

# tag = service-version
TAG         := config-server-0.0.3-SNAPSHOT

IMAGE_NAME  := $(DOCKER_USER)/$(PROJECT):$(TAG)

.PHONY: build image push

# 1. Package the JAR (repackages with dependencies)
build:
	mvn clean package spring-boot:repackage

# 2. Build the Docker image
image: build
	docker build -t $(IMAGE_NAME) .

# 3. Push it to Docker Hub
push: image
	docker push $(IMAGE_NAME)

# optionally, you can uncomment:
# run:
#   docker run -p 8070:8070 $(IMAGE_NAME)