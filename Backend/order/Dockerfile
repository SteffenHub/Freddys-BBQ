# 1. Builder
FROM gradle:8.10-jdk21-alpine AS builder

WORKDIR /app
ADD . /app

# remove '-PexcludeIT' to run the integrationstest ending with 'IT'. This is used, because there is a problem running docker container inside a docker container
# Uncomment if you want to run the intergrations tests with testcontainers inside docker.
# It is required to configure docker desktop and enable the setting: Expose daemon on tcp://localhost:2375 without TLS
#ENV DOCKER_HOST=tcp://host.docker.internal:2375
#ENV TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal
RUN gradle build --no-daemon -PexcludeIT


# 2. Spring Container
FROM eclipse-temurin:21

EXPOSE 8080

COPY --from=builder /app/build/libs/freddy_bbq_backend_order-0.0.1.jar /app/backend_order.jar

CMD ["java", "-jar", "/app/backend_order.jar"]