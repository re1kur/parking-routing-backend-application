FROM maven:3.9.9-eclipse-temurin-24-alpine AS build

COPY .. .

RUN mvn clean package -pl parking-service -am -DskipTests

FROM eclipse-temurin:24-jre-alpine

COPY --from=build /parking-service/target/pars-0.0.1-SNAPSHOT.jar parking-service.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "parking-service.jar"]
