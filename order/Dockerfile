# 1. Builder
FROM gradle:8.10-jdk21-alpine AS builder

WORKDIR /app
ADD . /app

RUN gradle build --no-daemon


# 2. Spring Container
FROM eclipse-temurin:21

# Install Dockerize
ENV DOCKERIZE_VERSION v0.7.0
RUN apt-get update \
    && apt-get install -y wget \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && apt-get autoremove -yqq --purge wget && rm -rf /var/lib/apt/lists/*

EXPOSE 8060

COPY --from=builder /app/build/libs/order-0.0.1-SNAPSHOT.jar /app/order.jar

CMD ["dockerize", "-wait", "http://keycloak:8080/realms/freddy/.well-known/openid-configuration", "-timeout", "60s", "java", "-jar", "/app/order.jar"]