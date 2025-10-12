# Ecommerce Microservices Project

This repository contains a microservices-based ecommerce platform built with Spring Boot and Java 21. The backend is organized into multiple services, and a simple frontend is provided.

## Project Structure

```
ecommerce-microservices/
  eureka-server/      # Service discovery with Netflix Eureka
  order-service/      # Handles order management
  product-service/    # Manages product catalog
  user-service/       # Manages user accounts
  payment-service/    # Manages payments of orders 
frontend-ecom/        # Frontend for the ecommerce platform
```

## Prerequisites

- Java 21
- Maven
- Docker (for containerization)
- PostgreSQL (used by product-service and others)

## Getting Started

### 1. Clone the repository

```sh
git clone https://github.com/yourusername/your-repo.git
cd your-repo
```

### 2. Build the project

```sh
cd ecommerce-microservices
mvn clean install
```

### 3. Run Databases with Docker Compose

Each service has its own `docker-compose.yml` for its database.  
To start a service’s database, run (for example):

Replace the password in `.env`

```sh
cd product-service
docker compose up -d
```

Repeat for `order-service` and `user-service` as needed.

### 4. Run the services

Start Eureka Server first:

```sh
cd eureka-server
mvn spring-boot:run
```

Then start other services (in separate terminals):

```sh
cd ../product-service
mvn spring-boot:run

cd ../order-service
mvn spring-boot:run

cd ../user-service
mvn spring-boot:run
```

### 5. Frontend

Open `frontend-ecom/frontpage.html` in your browser.

## CI/CD

GitHub Actions is used for CI. See [.github/workflows/product-service.yml](.github/workflows/product-service.yml) for the product service pipeline.

## Configuration

- Service dependencies and versions are managed in [ecommerce-microservices/pom.xml](ecommerce-microservices/pom.xml).
- Each service has its own `pom.xml` and configuration files.

## Remaining
- Admin DashBoard(Spark)
- Mail Service
- Images
- Wishlist

## Contributing

Contributions are welcome!  
Feel free to open issues or submit pull requests to improve the project.

## License

[MIT](LICENSE)

---
