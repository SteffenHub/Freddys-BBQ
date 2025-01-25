# 0. NPM Depedency Cache
FROM node:20-alpine AS npm_cacher
COPY package.json package-lock.json ./
RUN npm ci

# 1. Frontend Builder
FROM node:20-alpine AS builder
WORKDIR /app
ADD . /app
COPY --from=npm_cacher node_modules node_modules
RUN npm run build

# 2. Frontend Container
FROM caddy:2.8
EXPOSE 4200
COPY Caddyfile /etc/caddy/Caddyfile
RUN cat /etc/caddy/Caddyfile
COPY --from=builder /app/dist/shop-frontend/* /usr/share/caddy
