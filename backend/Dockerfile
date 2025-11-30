FROM gradle:8.5-jdk17 AS builder
WORKDIR /build

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .
COPY src src

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
