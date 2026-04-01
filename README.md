# E-commerce Backend

This is a Spring Boot backend for an e-commerce platform with user authentication, product management, cart, orders, and payment integration.

## Features

- JWT Authentication
- Product CRUD
- Cart Management
- Order Processing
- Stripe Payment Integration

## Setup

1. Clone the repository
2. Set your Stripe API key in application.properties
3. Run `mvn spring-boot:run`

## API Endpoints

- POST /api/auth/register
- POST /api/auth/login
- GET /api/products
- POST /api/products (Admin)
- GET /api/cart
- POST /api/cart/add
- POST /api/orders
- etc.

## Testing

Run `mvn test`
