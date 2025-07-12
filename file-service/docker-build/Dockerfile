FROM maven:3.9.9-eclipse-temurin-24-alpine AS build

COPY .. .

RUN mvn clean package -pl file-service -am -DskipTests

FROM eclipse-temurin:24-jre-alpine

COPY --from=build /file-service/target/fs-0.0.1-SNAPSHOT.jar file-service.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "file-service.jar"]