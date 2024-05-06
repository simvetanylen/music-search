FROM amazoncorretto:21.0.3
COPY build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]