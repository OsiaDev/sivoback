# -------- Stage 1: Build --------
FROM gradle:8.7-jdk17 AS builder

WORKDIR /app

# Copiamos solo archivos necesarios primero (mejora cache)
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

# Descargar dependencias (cacheable)
RUN ./gradlew dependencies --no-daemon || true

# Copiar código fuente
COPY src src

# Construir el jar
RUN ./gradlew bootJar --no-daemon

# -------- Stage 2: Runtime --------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Timezone Bogotá
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/America/Bogota /etc/localtime && \
    echo "America/Bogota" > /etc/timezone && \
    apk del tzdata

# Crear usuario no root (seguridad)
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar jar desde el builder
COPY --from=builder /app/build/libs/*.jar app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Duser.timezone=America/Bogota", "app.jar"]