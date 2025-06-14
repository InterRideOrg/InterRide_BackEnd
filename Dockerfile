
FROM openjdk:21-jdk-slim

ARG JAR_FILE=target/bookhub-api-0.0.1.jar

# Copia el archivo JAR en el contenedor
COPY ${JAR_FILE} bookhub-api.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar el archivo JAR
ENTRYPOINT ["java", "-jar", "bookhub-api.jar"]