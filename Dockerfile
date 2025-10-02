# ==== 1) Build stage ====
FROM maven:3.9-eclipse-temurin-21 AS build

# Arg modul (folder) & nama artefak
ARG MODULE_DIR=user-service
ARG JAR_NAME=user-service

# Workdir root repo
WORKDIR /workspace

# 1a. Copy hanya POM dulu untuk cache dependency
COPY pom.xml ./
COPY security-lib/pom.xml ./security-lib/pom.xml
COPY user-service/pom.xml ./user-service/pom.xml
COPY restaurant-service/pom.xml ./restaurant-service/pom.xml
COPY order-service/pom.xml ./order-service/pom.xml
COPY notification-service/pom.xml ./notification-service/pom.xml

# Pre-resolve dependencies (cache lebih awet)
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests -pl ${MODULE_DIR} -am dependency:go-offline

# 1b. Copy source code
COPY security-lib ./security-lib
COPY user-service ./user-service
COPY restaurant-service ./restaurant-service
COPY order-service ./order-service
COPY notification-service ./notification-service

# Build modul target saja (+ dependencies dari modul lain)
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests -pl ${MODULE_DIR} -am package

# ==== 2) Runtime stage ====
FROM eclipse-temurin:21-jre-alpine AS runtime
# security: non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app

# Salin jar hasil build
ARG MODULE_DIR=user-service
ARG JAR_NAME=user-service
COPY --from=build /workspace/${MODULE_DIR}/target/${JAR_NAME}*.jar /app/app.jar

# Port service (opsional, untuk dokumentasi)
EXPOSE 8081

# Opsi JVM default (bisa override via env JAVA_OPTS)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]