FROM amazoncorretto:21

COPY target/pet-med-application-*.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]