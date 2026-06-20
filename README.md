# Warehouse Inventory Demo

A Java 17 and Spring Boot 3 warehouse inventory demo. The project currently contains a 
REST API for categories, products, and stock movements.

## Run

```bash
mvn spring-boot:run
```

The API is available at http://localhost:8080.

## Data storage

No database is used at this stage. `WarehouseStore` keeps data in the application process memory, so the API is fully functional for demonstration purposes and all data is cleared when the application restarts.

## REST API

- `GET, POST, PUT, DELETE /api/categories`
- `GET, POST, PUT, DELETE /api/products`
- `GET /api/products?search=...&categoryId=...`
- `GET /api/movements`
- `GET /api/movements/product/{productId}`
- `POST /api/movements/product/{productId}`
