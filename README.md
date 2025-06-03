# Freddy's BBQ
Freddy's BBQ - Order & Delivery  
This project provides backend and frontend services for managing food orders and deliveries at Freddy's BBQ.  
It includes REST APIs for placing orders and tracking deliveries.

## 📌 Entry Points
- [Welcome Page (Menu)](https://freddys-bbq.onrender.com/)
  - Displays the available menu items and initiates an order
- [Delivery Overview (STAFF ONLY)](https://freddys-bbq.onrender.com/intern/delivery)
  - Internal dashboard for tracking deliveries
- [API Documentation (Swagger UI)](https://freddys-bbq.onrender.com/swagger-ui/index.html)
  - Provides OpenAPI documentation for available REST endpoints

## 🛠 Technologies Used
- Backend:
  - Java 21 (Spring Boot)
  - Spring Web & REST in a microservice architecture
  - Hibernate & Spring Data JPA (PostgreSQL)
  - OpenAPI documentation using Springdoc Swagger UI
  - Docker & Docker Compose for containerized deployment
  - Gradle for build automation
  - SMTP Mails
  - Deployment on Render.com
- Frontend:
  - Thymeleaf, HTML, CSS, JavaScript
  - Server-side rendering with Spring Boot & Thymeleaf
- Testing:
  - JUnit & Mockito for unit and integration tests
  - Testcontainers for database and service testing

## 🚀 Deploy Locally
### Run all Microservices using Docker Compose
You can run all microservices using `docker-compose.yml`.
Ensure you have Docker Desktop installed and running.
```sh
  docker-compose up
```
After running the command, the following services will be available:
- Customer Frontend → [http://localhost:4200](http://localhost:4200)
- Staff Frontend → [http://localhost:4300](http://localhost:4300)
- Order Backend (API) → [http://localhost:8080](http://localhost:8080)
- Delivery Backend (API) → [http://localhost:8081](http://localhost:8081)

### Running the Release Version
To save costs when hosting on Render.com, the microservices are combined into a single deployment.  
The release version merges all required services into one application (see `Dockerfile` in the root directory).

To start the release version:
```sh
  docker-compose -f docker-compose-release.yml up
```
