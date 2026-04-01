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
2. Set environment variables for security:
   - `JWT_SECRET`: A secure 64-byte (512-bit) secret key for JWT signing. Generate using: `openssl rand -base64 64` or secure random generation.
   - `STRIPE_API_KEY`: Your Stripe secret API key (starts with `sk_test_` for test mode).
   Example:
   ```
   export JWT_SECRET="your-generated-64-byte-secret-here"
   export STRIPE_API_KEY="sk_test_your_stripe_key_here"
   ```
3. Update database configuration in `application.properties` if needed (default is MySQL).
4. Run `mvn spring-boot:run`

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
