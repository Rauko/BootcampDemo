# Warehouse Inventory Demo

A Java 17 and Spring Boot 3 warehouse inventory demo. The project currently contains a 
REST API for categories, products, and stock movements.

## Run

```bash
mvn spring-boot:run
```

The API is available at http://localhost:8080.

## Database setup

The application uses PostgreSQL with Spring Data JPA. Flyway applies database migrations automatically at application startup. The first migration is located at `src/main/resources/db/migration/V1__create_warehouse_schema.sql`.

Create a database and an application user:

```sql
CREATE USER warehouse_app WITH PASSWORD 'your_secure_password';
CREATE DATABASE warehouse_inventory OWNER warehouse_app;
```

Provide the password before starting the application:

```powershell
$env:DB_PASSWORD = "your_secure_password"
mvn spring-boot:run
```

Optional environment variables: `DB_URL` and `DB_USERNAME`. Their defaults are `jdbc:postgresql://localhost:5432/warehouse_inventory` and `warehouse_app`.

## REST API

- `GET, POST, PUT, DELETE /api/categories`
- `GET, POST, PUT, DELETE /api/products`
- `GET /api/products?search=...&categoryId=...`
- `GET /api/movements`
- `GET /api/movements/product/{productId}`
- `POST /api/movements/product/{productId}`
