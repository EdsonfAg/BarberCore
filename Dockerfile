# ---------------------------------------------------------------------
# ETAPA 1: Compilación y empaquetado con Maven y JDK 21
# ---------------------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copiar pom.xml y descargar dependencias en caché
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y generar el archivo ejecutable barbercore.jar
COPY src ./src
RUN mvn clean package -DskipTests

# ---------------------------------------------------------------------
# ETAPA 2: Imagen ligera de ejecución (Solo JRE 21)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el JAR compilado desde la etapa 1
COPY --from=builder /app/target/barbercore.jar app.jar

# Exponer el puerto para el equipo de Diseño Web / API
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
