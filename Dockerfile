
FROM openjdk:21-jdk-slim

ARG JAR_FILE=target/interride-api-0.0.1.jar


COPY ${JAR_FILE} interride-api.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar el archivo JAR
ENTRYPOINT ["java", "-jar", "interride-api.jar.jar"]