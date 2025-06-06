To run the Jenkins Docker Container use this command:

```sh
  docker build -t myjenkins-blueocean:1 .
```
After the container build create a network named 'jenkins' and run the docker container:

```sh
docker create network jenkins
```
```sh
docker run --name jenkins-blueocean --restart=on-failure --detach \
--network jenkins \
--publish 8082:8082 --publish 50000:50000 \
--volume jenkins-data:/var/jenkins_home \
--volume jenkins-docker-certs:/certs/client:ro \
myjenkins-blueocean:1
```


### If you configure a Docker Cloud  
You can use this container setup to use it as docker Host URI with 'tcp://docker-proxy:2375' 
```sh
docker run -d \
  --name docker-proxy \
  --network jenkins \
  --restart=always \
  -p 127.0.0.1:2376:2375 \
  -v /var/run/docker.sock:/var/run/docker.sock \
  alpine/socat \
  tcp-listen:2375,fork,reuseaddr \
  unix-connect:/var/run/docker.sock
```
Make sure you use the Expose DOCKER_HOST option

### As Docker Agent this setup will work:

Labels: docker-agent-java-alpine-21  
Docker Image: jenkins/agent:alpine-jdk21  
User: root  
Network: jenkins  
Remote File System Root: /home/jenkins  