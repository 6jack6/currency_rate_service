FROM maven:3.9.9-eclipse-temurin-11 AS build

ARG PACT_BROKER_URL=http://host.docker.internal:9292

WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn -q verify -Dpactbroker.url=${PACT_BROKER_URL}

FROM eclipse-temurin:11-jre

WORKDIR /app
COPY --from=build /app/target/currency-rate-provider-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
