# 📚 Bookstore Management System

A production-ready backend system for an end-to-end bookstore platform, built independently from scratch. Features JWT authentication, role-based access control, and a fully tested Service and Controller layer.

🔗 **Live API (Swagger UI):** https://bookstore-api-mx23.onrender.com/swagger-ui/index.html
💻 **GitHub:** https://github.com/lekha-bot/bookstore-management-system

---

## 🛠️ Tech Stack

- **Language:** Java
- **Framework:** Spring Boot, Spring Security, Spring Data JPA
- **Database:** MySQL (hosted on Aiven Cloud)
- **Authentication:** JWT (JSON Web Tokens), BCrypt password encryption
- **Testing:** JUnit 5, Mockito, Spring Security Test
- **API Docs:** Swagger / OpenAPI
- **Build Tool:** Maven
- **Deployment:** Render

---

## ✨ Features

- 👤 **User Management** — registration, retrieval, and deletion of users with encrypted passwords
- 📦 **Product Management** — full CRUD operations on products (Admin-restricted create/update/delete)
- 🛒 **Cart Management** — add to cart, view cart by user, remove cart items, with `@ManyToOne` relationships to User and Product
- 🔐 **Authentication** — secure login with JWT token generation and BCrypt password verification
- 🛡️ **Role-Based Access Control** — `ADMIN` and `USER` roles enforced via Spring Security `@PreAuthorize`
- ⚠️ **Global Exception Handling** — structured JSON error responses with appropriate HTTP status codes
- 📖 **API Documentation** — interactive Swagger UI for exploring and testing endpoints

---

## 🚀 API Endpoints

### 🔐 Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | Authenticate user and return JWT token |

### 👤 Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users` | Create a new user |
| GET | `/users` | Get all users |
| GET | `/users/{id}` | Get user by ID |
| DELETE | `/users/{id}` | Delete user (Admin only) |

### 📦 Products
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/products` | Create a product | Admin |
| GET | `/products` | Get all products | User, Admin |
| GET | `/products/{id}` | Get product by ID | Authenticated |
| PUT | `/products/{id}` | Update a product | Admin |
| DELETE | `/products/{id}` | Delete a product | Admin |

### 🛒 Cart
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/cart` | Add item to cart |
| GET | `/cart/{userId}` | Get cart items for a user |
| DELETE | `/cart/{cartId}` | Remove item from cart |

---

## ✅ Testing

The project includes a full unit and integration test suite covering both the Service and Controller layers:

**🧪 Service Layer (JUnit 5 + Mockito)**
- `UserServiceImplTest`
- `ProductServiceImplTest`
- `CartServiceImplTest`
- `AuthServiceImplTest`

Tests cover successful operations, not-found exception handling, and edge cases (e.g., invalid password, missing related entities) using mocked repositories.

**🌐 Controller Layer (Spring MVC Test + MockMvc)**
- `UserControllerTest`
- `ProductControllerTest`
- `CartControllerTest`
- `AuthControllerTest`

Tests verify HTTP status codes, JSON response structure, and role-based access enforcement (`@PreAuthorize`) using `@WebMvcTest`, `@MockitoBean`, and `@WithMockUser`.

### ▶️ Running the tests

```bash
mvn test
```

To run a specific test class:

```bash
mvn test -Dtest=ProductControllerTest
```

---

## 🏗️ Architecture

The project follows a clean, layered architecture:

```
Controller → Service → Repository → Database
```

- **DTOs** decouple API contracts from entity models
- **Global exception handler** centralizes error responses
- **Security layer** (`JwtFilter`, `JwtUtil`, `SecurityConfig`) handles authentication and authorization independently of business logic

---

## ⚙️ Local Setup

1. Clone the repository
   ```bash
   git clone https://github.com/lekha-bot/bookstore-management-system.git
   ```
2. Configure your MySQL database credentials in `application.properties`
3. Build and run
   ```bash
   mvn spring-boot:run
   ```
4. Access Swagger UI at `http://localhost:8080/swagger-ui/index.html`

---

## 👩‍💻 Author

**Lekha Jothi**
Java Backend Developer | Spring Boot | REST APIs | MySQL | JWT
🔗 [LinkedIn](https://linkedin.com/in/lekha-jothi) · 💻 [GitHub](https://github.com/lekha-bot)
