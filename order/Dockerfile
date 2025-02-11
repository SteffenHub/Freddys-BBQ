FROM eclipse-temurin:21 AS builder

WORKDIR /app

ADD . /app

RUN ./gradlew build


FROM eclipse-temurin:21

EXPOSE 8060

COPY --from=builder /app/build/libs/order-0.0.1-SNAPSHOT.jar /app/order.jar

CMD ["java", "-jar", "/app/order.jar"]