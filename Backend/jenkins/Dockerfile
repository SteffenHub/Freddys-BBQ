FROM jenkins/jenkins:latest-jdk21

USER root

RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli

ENV DOCKER_HOST=tcp://docker:2376
ENV DOCKER_CERT_PATH=/certs/client
ENV DOCKER_TLS_VERIFY=1

EXPOSE 8082
EXPOSE 50000

VOLUME /var/jenkins_home

USER jenkins

RUN jenkins-plugin-cli --plugins "blueocean docker-workflow token-macro json-path-api"
