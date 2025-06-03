# 1. Builder
FROM gradle:8.10-jdk21-alpine AS builder

WORKDIR /app
ADD ./Release /app

# Config
COPY Backend/order/src/main/java/com/freddys_bbq_order/config /app/src/main/java/com/

# Backend Delivery
COPY Backend/delivery/src/main/java/com/freddys_bbq_delivery /app/src/main/java/com/freddys_bbq_delivery
RUN rm -f /app/src/main/java/com/freddys_bbq_delivery/DemoApplication.java

# Backend Order
COPY Backend/order/src/main/java/com/freddys_bbq_order /app/src/main/java/com/freddys_bbq_order
RUN rm -f /app/src/main/java/com/freddys_bbq_order/DemoApplication.java
RUN rm -rf /app/src/main/java/com/freddys_bbq_order/config

# Backend Mail
COPY Backend/mail/src/main/java/com/freddys_bbq_mail /app/src/main/java/com/freddys_bbq_mail
RUN rm -f /app/src/main/java/com/freddys_bbq_mail/DemoApplication.java

# Frontend Customer
COPY Frontend/customer/src/main/java/com/freddys_bbq_frontend_customer /app/src/main/java/com/freddys_bbq_frontend_customer
RUN rm -f /app/src/main/java/com/freddys_bbq_frontend_customer/DemoApplication.java
RUN rm -rf /app/src/main/java/com/freddys_bbq_frontend_customer/config
COPY Frontend/customer/src/main/resources/templates/index.html /app/src/main/resources/templates
COPY Frontend/customer/src/main/resources/templates/order.html /app/src/main/resources/templates
COPY Frontend/customer/src/main/resources/templates/order-info.html /app/src/main/resources/templates

# Frontend Intern
COPY Frontend/Intern/src/main/java/com/freddys_bbq_frontend_intern /app/src/main/java/com/freddys_bbq_frontend_intern
RUN rm -f /app/src/main/java/com/freddys_bbq_frontend_intern/DemoApplication.java
RUN rm -rf /app/src/main/java/com/freddys_bbq_frontend_intern/config
COPY Frontend/Intern/src/main/resources/templates/delivery.html /app/src/main/resources/templates
COPY Frontend/Intern/src/main/resources/templates/map.html /app/src/main/resources/templates

RUN gradle build --no-daemon

# 2. Spring Container
FROM eclipse-temurin:21

EXPOSE 8080

COPY --from=builder /app/build/libs/freddy_bbq-0.0.1.jar /app/freddy_bbq.jar

CMD ["java", "-jar", "/app/freddy_bbq.jar"]
