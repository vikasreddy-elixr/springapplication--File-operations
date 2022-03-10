FROM openjdk:17
COPY build/libs/*.jar springapplicationforfileoperation.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","springapplicationforfileoperation.jar"]
