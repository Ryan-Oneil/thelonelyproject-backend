FROM adoptopenjdk/openjdk16:alpine
VOLUME /lonelyproject
ADD /target/*.jar lonelyproject.jar
ENTRYPOINT ["java","-jar","/lonelyproject.jar", "--spring.profiles.active=local"]
EXPOSE 8080