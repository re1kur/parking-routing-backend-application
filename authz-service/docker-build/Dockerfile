FROM maven:3.9.9-eclipse-temurin-24-alpine AS build

COPY .. .

RUN mvn clean package -pl authz-service -am -DskipTests

FROM eclipse-temurin:24-jre-alpine

COPY --from=build /authz-service/target/authz-0.0.1-SNAPSHOT.jar authz-service.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "authz-service.jar"]