# 1. Builder
FROM golang:1.23-alpine AS builder
WORKDIR /app
ADD . /app
RUN CGO_ENABLED=0 go build -ldflags="-w -s" -o build/kitchen .

# 2. Container
FROM alpine:3.20
COPY --from=builder /app/build/kitchen /usr/bin/

# Install Dockerize
ENV DOCKERIZE_VERSION v0.7.0
RUN apk update --no-cache \
    && apk add --no-cache wget openssl \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && apk del wget

EXPOSE 9090

CMD ["dockerize", "-wait", "tcp://rabbitmq:5672", "-timeout", "60s", "/usr/bin/kitchen"]