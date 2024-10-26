FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/polynomial-processing-service-0.0.1-SNAPSHOT.jar polynomial-processing-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "polynomial-processing-service.jar"]