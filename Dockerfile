FROM openjdk:17
LABEL maintainer="vikas"
ADD spring-boot-application-with-docker.jar spring-boot-docker-application.jar
ENTRYPOINT ["java", "-jar", "spring-boot-docker-application.jar"]
