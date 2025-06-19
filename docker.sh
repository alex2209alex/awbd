docker build -t vladandries/gateway:latest ./gateway
docker build -t vladandries/awbd-be:latest ./awbd-app/awbd-be
docker build -t vladandries/awbd-fe:latest ./awbd-app/awbd-fe
docker build -t vladandries/config-server:latest ./config-server
docker build -t vladandries/eureka-server:latest ./eureka-server

docker push vladandries/gateway:latest
docker push vladandries/awbd-be:latest
docker push vladandries/awbd-fe:latest
docker push vladandries/config-server:latest
docker push vladandries/eureka-server:latest