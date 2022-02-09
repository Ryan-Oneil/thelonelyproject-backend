FROM openjdk:16.0-jdk-alpine
VOLUME /lonelyproject
ADD /target/*.jar lonelyproject.jar
ENTRYPOINT ["java","-jar","/lonelyproject.jar", "--spring.profiles.active=local"]
EXPOSE 8080