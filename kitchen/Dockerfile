# 1. Builder
FROM golang:1.23-alpine AS builder
WORKDIR /app
ADD . /app
RUN CGO_ENABLED=0 go build -ldflags="-w -s" -o build/kitchen .

# 2. Container
FROM alpine:3.20
COPY --from=builder /app/build/kitchen /usr/bin/

EXPOSE 8070

CMD ["/usr/bin/kitchen"]