FROM openjdk:21-jdk-slim

WORKDIR /app

# Notwendige Tools installieren
RUN apt-get update && apt-get install -y curl

RUN curl -L -o /app/ors.jar https://github.com/GIScience/openrouteservice/releases/download/v9.1.0/ors.jar

COPY ors-config.yml /app

# OSM-Datei herunterladen
RUN mkdir -p /app/files && curl -o /app/files/hamburg-latest.osm.pbf https://download.geofabrik.de/europe/germany/hamburg-latest.osm.pbf

EXPOSE 8090

CMD ["java", "-jar", "ors.jar", "ors-config.yml"]
