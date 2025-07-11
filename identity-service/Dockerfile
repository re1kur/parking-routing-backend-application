FROM maven:3.9.9-eclipse-temurin-24-alpine AS build

COPY . .

RUN mvn clean package -pl identity-service -am -DskipTests

FROM eclipse-temurin:24-jre-alpine

COPY --from=build /identity-service/target/is-0.0.1-SNAPSHOT.jar identity-service.jar

ENTRYPOINT ["java", "-jar", "identity-service.jar"]