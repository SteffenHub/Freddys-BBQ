# 1. Builder
FROM gradle:8.10-jdk21-alpine AS builder

WORKDIR /app
ADD . /app

RUN gradle build --no-daemon


# 2. Spring Container
FROM eclipse-temurin:21

EXPOSE 8060

COPY --from=builder /app/build/libs/order-0.0.1-SNAPSHOT.jar /app/order.jar

CMD ["java", "-jar", "/app/order.jar"]