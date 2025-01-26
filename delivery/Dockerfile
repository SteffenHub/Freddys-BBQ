# 1. Builder
FROM gradle:8.10-jdk21-alpine AS builder

WORKDIR /app
ADD . /app

RUN gradle build --no-daemon


# 2. Spring Container
FROM eclipse-temurin:21

EXPOSE 8050

# Install Dockerize
ENV DOCKERIZE_VERSION v0.7.0
RUN apt-get update \
    && apt-get install -y wget \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && apt-get autoremove -yqq --purge wget && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/build/libs/delivery-0.0.1-SNAPSHOT.jar /app/delivery.jar

ENTRYPOINT ["dockerize", "-wait", "tcp://rabbitmq:5672", "-timeout", "60s", "java", "-jar", "/app/delivery.jar"]