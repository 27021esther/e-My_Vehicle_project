# 🚗 Vehicle Trading System

A RESTful backend API for buying and selling vehicles, built with **Spring Boot 3**, **Spring Security**, **JWT authentication**, and **OAuth2 social login** (Google & GitHub).

---

## 🧰 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Security | Spring Security + JWT (JJWT 0.12.5) |
| Social Login | OAuth2 (Google, GitHub) |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Docs | SpringDoc OpenAPI (Swagger UI) |
| Utilities | Lombok |

---

## 📁 Project Structure

```
src/main/java/com/vehicletrading/
├── config/
│   ├── SecurityConfig.java         # Security rules, CORS, JWT filter chain
│   ├── CorsConfig.java
│   ├── PasswordEncoderConfig.java
│   ├── SwaggerConfig.java
│   └── GlobalExceptionHandler.java
├── controller/
│   ├── AuthController.java         # Register, Login, OAuth2 callback
│   ├── VehicleController.java
│   ├── ListingController.java
│   └── UserController.java
├── model/
│   ├── User.java
│   ├── Vehicle.java
│   └── Listing.java
├── dto/                            # Request/Response DTOs
├── enums/
│   ├── UserRole.java               # ROLE_USER, ROLE_ADMIN
│   ├── VehicleType.java            # CAR, TRUCK, MOTORCYCLE, VAN, SUV, BUS, OTHER
│   ├── VehicleStatus.java          # AVAILABLE, SOLD, RESERVED, UNDER_MAINTENANCE
│   └── ListingStatus.java          # ACTIVE, CLOSED, PENDING
├── repository/
├── security/
│   ├── JwtUtil.java
│   ├── JwtAuthenticationFilter.java
│   └── OAuth2SuccessHandler.java
└── service/
```

---

## ⚙️ Setup & Configuration

### 1. Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL running locally

### 2. Create the Database

```sql
CREATE DATABASE vehicle_trading_db;
```

### 3. Configure `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/vehicle_trading_db
    username: your_postgres_username
    password: your_postgres_password

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_GOOGLE_CLIENT_ID
            client-secret: YOUR_GOOGLE_CLIENT_SECRET
          github:
            client-id: YOUR_GITHUB_CLIENT_ID
            client-secret: YOUR_GITHUB_CLIENT_SECRET

app:
  jwt:
    secret: YOUR_BASE64_SECRET
    expiration: 900000   # 15 minutes

server:
  port: 8081
```

### 4. Run the Application

```bash
cd vehicle-trading-system
mvn clean package -DskipTests
mvn spring-boot:run
```

---

## 🔐 Authentication

### JWT (Local)

Register and login to receive a JWT token. Include it in all protected requests:

```
Authorization: Bearer <your_token>
```

### OAuth2 (Social Login)

Open in your **browser** (not Swagger):

| Provider | URL |
|---|---|
| Google | `http://localhost:8081/oauth2/authorization/google` |
| GitHub | `http://localhost:8081/oauth2/authorization/github` |

On success, you are redirected to:
```
GET /api/auth/oauth2/success?token=<jwt>
```
Use the returned JWT for all subsequent requests.

#### OAuth2 Redirect URIs to Register

**Google Cloud Console** → APIs & Services → Credentials → your OAuth client:
```
http://localhost:8081/login/oauth2/code/google
```

**GitHub** → Settings → Developer Settings → OAuth Apps → your app:
```
http://localhost:8081/login/oauth2/code/github
```

---

## 🛡️ Authorization Rules

| Resource | GET | POST | PUT | DELETE |
|---|---|---|---|---|
| `/api/auth/**` | Public | Public | — | — |
| `/api/vehicles/**` | Public | Authenticated | Authenticated | ADMIN only |
| `/api/listings/**` | Public | Authenticated | Authenticated | ADMIN only |
| `/api/users/**` | Authenticated | — | Authenticated | ADMIN only |

---

## 📡 API Endpoints

### Auth — `/api/auth`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Login and receive JWT |
| GET | `/api/auth/oauth2/success` | Public | OAuth2 callback — returns JWT |

### Vehicles — `/api/vehicles`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/vehicles` | Public | Get all vehicles |
| GET | `/api/vehicles/{id}` | Public | Get vehicle by ID |
| GET | `/api/vehicles/status/{status}` | Public | Filter by status |
| GET | `/api/vehicles/owner/{ownerId}` | Authenticated | Get vehicles by owner |
| POST | `/api/vehicles` | Authenticated | Add a new vehicle |
| PUT | `/api/vehicles/{id}` | Authenticated | Update a vehicle |
| DELETE | `/api/vehicles/{id}` | ADMIN | Delete a vehicle |

**Vehicle statuses:** `AVAILABLE` · `SOLD` · `RESERVED` · `UNDER_MAINTENANCE`

**Vehicle types:** `CAR` · `TRUCK` · `MOTORCYCLE` · `VAN` · `SUV` · `BUS` · `OTHER`

### Listings — `/api/listings`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/listings` | Public | Get all listings |
| GET | `/api/listings/{id}` | Public | Get listing by ID |
| GET | `/api/listings/status/{status}` | Public | Filter by status |
| GET | `/api/listings/seller/{sellerId}` | Authenticated | Get listings by seller |
| POST | `/api/listings` | Authenticated | Create a listing |
| PUT | `/api/listings/{id}` | Authenticated | Update a listing |
| DELETE | `/api/listings/{id}` | ADMIN | Delete a listing |

**Listing statuses:** `ACTIVE` · `CLOSED` · `PENDING`

### Users — `/api/users`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/users` | Authenticated | Get all users |
| GET | `/api/users/me` | Authenticated | Get current user profile |
| GET | `/api/users/{id}` | Authenticated | Get user by ID |
| PUT | `/api/users/{id}` | Authenticated | Update user |
| DELETE | `/api/users/{id}` | ADMIN | Delete user |

---

## 📊 Data Model

```
User
 ├── id, username, email, password
 ├── role: ROLE_USER | ROLE_ADMIN
 ├── provider: local | google | github
 ├── providerId
 ├── vehicles[]  →  Vehicle
 └── listings[]  →  Listing

Vehicle
 ├── id, make, model, year, color, mileage, price
 ├── type: CAR | TRUCK | MOTORCYCLE | VAN | SUV | BUS | OTHER
 ├── status: AVAILABLE | SOLD | RESERVED | UNDER_MAINTENANCE
 ├── owner  →  User
 └── listings[]  →  Listing

Listing
 ├── id, title, description, askingPrice, createdAt
 ├── status: ACTIVE | CLOSED | PENDING
 ├── vehicle  →  Vehicle
 └── seller   →  User
```

---

## 📖 Swagger UI

After starting the app, open:

```
http://localhost:8081/swagger-ui.html
```

To test protected endpoints in Swagger:
1. Call `POST /api/auth/login` to get a token
2. Click **Authorize** at the top right
3. Enter: `Bearer <your_token>`

---

## 🧪 Example: Register & Login

**Register**
```bash
POST /api/auth/register
{
  "username": "Esther",
  "email": "eiradukunda874@gmail.com",
  "password": "secret123"
}
```

**Login**
```bash
POST /api/auth/login
{
  "username": "Esther",
  "password": "27021"
}
```

**Response**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "ESTHER",
  "role": "ADMIN_USER"
}
```

---

## 📝 Notes

- JWT tokens expire after **15 minutes** by default (configurable via `app.jwt.expiration`)
- OAuth2 users are automatically registered on first login with `ROLE_USER`
- GitHub OAuth2 users without a public email get a placeholder email (`login@github.com`)
- CSRF is disabled — this API is stateless and token-based
