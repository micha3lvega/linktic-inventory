# Linktic Inventory System

Este proyecto es una prueba técnica que simula un sistema de inventario basado en microservicios, desarrollado con Spring Boot, utilizando autenticación por API Key y comunicación HTTP entre servicios.

## 🌐 Tecnologías utilizadas

- Java 17
- Spring Boot 3.5
- Spring Web, Data JPA, H2
- Docker y Docker Compose
- Swagger (OpenAPI)
- ModelMapper
- JUnit 5 y Mockito

## 📅 Estructura del proyecto

```
linktic-inventory/
├── docker-compose.yml
├── .env
├── products-application/
│   └── products-services/
│       └── Dockerfile
├── inventory-application/
    └── inventory-services/
        └── Dockerfile
```

## ⚖️ Microservicios

### products-services
- Gestiona los productos disponibles.
- Expuesto en `localhost:8080`
- Protegido con `x-api-key`.

### inventory-services
- Gestiona el stock de productos y permite registrar compras.
- Consulta el microservicio de productos para validar existencia.
- Expuesto en `localhost:8081`
- Protegido con `x-api-key`.

## 🔐 Seguridad

Cada microservicio está protegido con una clave API mediante el header `x-api-key`.

### Variables de entorno (en `.env`):
```env
security.api-key=secret-key-inventory-123
secret-product-api-key=secret-key-products-asdfg
products.base-url=http://products-service:8080/api/v1/product
```

## ⚙️ Uso con Docker Compose

### 1. Genera los JARs
```bash
mvn clean package -DskipTests
```

### 2. Levanta los servicios
```bash
docker-compose up --build
```

## 📊 Endpoints principales

### Inventory
```
GET     /api/v1/inventory/{productId}/stock         # Consultar stock
PUT     /api/v1/inventory/{productId}/stock?quantity=10
POST    /api/v1/inventory/purchase                  # Registra compra
```

### Products
```
GET     /api/v1/product/{id}                        # Consultar producto
POST    /api/v1/product                              # Crear producto
```

## 💡 Swagger
- Productos: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Inventario: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

Agregar `x-api-key` desde el botón de Authorize ✉️.

## 🎓 Pruebas
- Pruebas unitarias disponibles para servicios principales.
- Las pruebas de integración están pendientes por tiempo.

## ✉️ Contacto
Desarrollado por Michael Vega como prueba técnica para Linktic.
