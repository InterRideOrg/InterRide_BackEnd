
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/interride-api-0.0.1.jar interride-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "interride-api.jar"]