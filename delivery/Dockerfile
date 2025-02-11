# 1. Builder
FROM gradle:8.10-jdk21-alpine AS builder

WORKDIR /app
ADD . /app

RUN gradle build --no-daemon


# 2. Spring Container
FROM eclipse-temurin:21

EXPOSE 8050

COPY --from=builder /app/build/libs/delivery-0.0.1-SNAPSHOT.jar /app/delivery.jar

CMD ["java", "-jar", "/app/delivery.jar"]