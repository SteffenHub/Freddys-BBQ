FROM alpine:3.20

ADD oauth2-proxy.cfg /var/oauth2-proxy.cfg

RUN echo "@testing https://dl-cdn.alpinelinux.org/alpine/edge/testing" >> /etc/apk/repositories

RUN apk update --no-cache \
    && apk add oauth2-proxy@testing

# Install Dockerize
ENV DOCKERIZE_VERSION v0.7.0
RUN apk update --no-cache \
    && apk add --no-cache wget openssl \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && apk del wget

CMD ["dockerize", "-wait", "http://keycloak:8080/realms/freddy/.well-known/openid-configuration", "-timeout", "60s", "oauth2-proxy", "--config", "/var/oauth2-proxy.cfg"]