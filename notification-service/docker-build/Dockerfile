FROM maven:3.9.9-eclipse-temurin-24-alpine AS build

COPY .. .

RUN mvn clean package -pl notification-service -am -DskipTests

FROM eclipse-temurin:24-jre-alpine

COPY --from=build /notification-service/target/ns-0.0.1-SNAPSHOT.jar notification-service.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "notification-service.jar"]