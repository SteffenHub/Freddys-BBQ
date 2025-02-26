# 1. Builder
FROM gradle:8.10-jdk21-alpine AS builder

WORKDIR /app
ADD . /app

RUN gradle build --no-daemon


# 2. Spring Container
FROM eclipse-temurin:21

EXPOSE 4200

COPY --from=builder /app/build/libs/freddy_bbq_frontend_customer-0.0.1.jar /app/frontend_customer.jar

CMD ["java", "-jar", "/app/frontend_customer.jar"]