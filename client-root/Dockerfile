# Utiliza una imagen oficial de Java como base
FROM eclipse-temurin:21-jdk-alpine

# Establece un directorio de trabajo dentro del contenedor
WORKDIR /app

COPY target/client-root-0.0.1-SNAPSHOT.jar app-client-0.0.1.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app-client-0.0.1.jar"]