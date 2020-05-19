FROM openjdk:8-jre-alpine
ADD target/orchestTest-0.0.1-SNAPSHOT.jar /orchestTest.jar
WORKDIR /
RUN apk add --no-cache curl
#HEALTHCHECK CMD /usr/bin/curl --fail http://localhost:8080/healthy || exit 1
CMD ["java", "-jar", "orchestTest.jar"]
