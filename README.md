# orchest_test
It is a demo of a REST API for the insertion and organization of worker signings.
It is developed with Spring Boot 2.2.7.RELEASE and Java 8.

## Docker deployment
It is only necessary to be located within the project folder and execute build_docker_image.sh script to create 
orchest_test image, then copy example.env to .env and modify parameters if it is necessary, and raise app with docker compose. 
[Docker](https://docs.docker.com/get-docker/) and [docker-compose](https://docs.docker.com/compose/install/) must be installed 

```shell script
./build_docker_image.sh
cp ./example.env ./.env
docker-compose up 
```