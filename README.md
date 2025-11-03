# 💰 Finance Flow - Personal Finance Tracker Backend

**A powerful REST API for personal finance management** - Track expenses, manage budgets, and analyze spending patterns with ease!

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat-square&logo=java)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.0-6DB33F?style=flat-square&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=flat-square&logo=apache-maven)](https://maven.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-336791?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-Authentication-000000?style=flat-square)](https://jwt.io/)

---

## 📋 **Spis Treści**
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [API Endpoints](#-api-endpoints)
- [Database Schema](#-database-schema)
- [Authentication](#-authentication)
- [Error Handling](#-error-handling)
- [Contributing](#-contributing)

---

## ✨ **Features**

### 🔐 **Authentication & Security**
- JWT token-based authentication
- Secure password hashing (BCrypt)
- Role-based access control (USER, ADMIN)
- User registration and login

### 💳 **Transaction Management**
- Create, read, update, delete transactions
- Categorize income and expenses
- Filter by date range, category, and type
- Pagination support
- Recent transactions endpoint

### 💰 **Budget Management**
- Set monthly budgets per category
- Track budget status and spending
- Alert system for budget overruns
- Global and category-specific budgets
- Budget vs actual analysis

### 🏷️ **Category Management**
- Pre-defined default categories
- Custom category creation
- Support for income and expense categories
- Color and icon customization
- Category-based filtering

### 📊 **Additional Features**
- Comprehensive error handling
- API request/response validation
- Transaction logging
- Database migrations (Flyway)
- Pagination and sorting
- OpenAPI/Swagger documentation ready

---

## 🛠️ **Tech Stack**

### Backend
- **Framework:** Spring Boot 3.1.0
- **Language:** Java 17
- **Build Tool:** Maven 3.8+
- **Database:** PostgreSQL 14+
- **ORM:** JPA/Hibernate

### Security
- **Authentication:** JWT (JSON Web Tokens)
- **Password Encoding:** BCrypt
- **Authorization:** Spring Security 6.1+

### Libraries
- **Lombok** - Reduce boilerplate code
- **Flyway** - Database migrations
- **Jakarta Persistence API** - ORM
- **Jackson** - JSON processing
- **Validation** - Jakarta Bean Validation

### Testing
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking library
- **H2 Database** - In-memory testing database

---

## 📁 **Project Structure**


```finance-flow-backend/
├── src/main/java/finance_flow/Finance_Flow/
│   ├── config/                 # Spring configurations
│   │   ├── SecurityConfig.java
│   │   └── AppConfig.java
│   │
│   ├── controller/             # REST API endpoints
│   │   ├── AuthController.java
│   │   ├── TransactionController.java
│   │   ├── BudgetController.java
│   │   └── CategoryController.java
│   │
│   ├── service/                # Business logic
│   │   ├── AuthService.java
│   │   ├── TransactionService.java
│   │   ├── BudgetService.java
│   │   ├── CategoryService.java
│   │   └── impl/               # Service implementations
│   │
│   ├── repository/             # Data access layer
│   │   ├── UserRepository.java
│   │   ├── TransactionRepository.java
│   │   ├── BudgetRepository.java
│   │   └── CategoryRepository.java
│   │
│   ├── model/                  # Entity classes
│   │   ├── User.java
│   │   ├── Transaction.java
│   │   ├── Budget.java
│   │   ├── Category.java
│   │   └── enums/
│   │
│   ├── dto/                    # Data Transfer Objects
│   │   ├── request/            # Request DTOs
│   │   └── response/           # Response DTOs
│   │
│   ├── security/               # Security components
│   │   ├── JwtTokenProvider.java
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── CustomUserDetailsService.java
│   │   └── UserPrincipal.java
│   │
│   ├── exception/              # Exception handling
│   │   ├── ResourceNotFoundException.java
│   │   ├── BadRequestException.java
│   │   ├── UnauthorizedException.java
│   │   └── GlobalExceptionHandler.java
│   │
│   └── util/                   # Utility classes
│       ├── SecurityUtils.java
│       └── DefaultCategoriesGenerator.java
│
├── src/main/resources/
│   ├── application.properties
│   └── db/migration/           # Flyway migrations
│       ├── V1__Create_users_table.sql
│       ├── V2__Create_categories_table.sql
│       ├── V3__Create_transactions_table.sql
│       └── V4__Create_budgets_table.sql
│
└── pom.xml                     # Maven dependencies
```


---

## 🚀 **Getting Started**

### Prerequisites
- Java 17 or higher
- Maven 3.8 or higher
- PostgreSQL 14 or higher
- Git

### Installation

1. **Clone the repository**

```
git clone https://github.com/yourusername/finance-flow-backend.git
cd finance-flow-backend
```


2. **Create PostgreSQL database**

```
CREATE DATABASE finance_flow_db;
```

3. **Configure application.properties**

# Database Configuration
```
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_flow_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

# JWT Configuration
```
app.jwt.secret=YourSuperSecretKeyForJWTTokenGenerationMinimum512BitsLongSecretKey123456789
app.jwt.expiration=86400000
```
# Hibernate/JPA
```
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

4. **Build the project**
```
mvn clean install
```

5. **Run the application**
```
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## 📡 **API Endpoints**

### 🔐 Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register new user |
| POST | `/api/v1/auth/login` | Login and get JWT token |

### 💳 Transactions
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/transactions` | Create transaction |
| GET | `/api/v1/transactions` | Get all transactions (paginated) |
| GET | `/api/v1/transactions/{id}` | Get transaction by ID |
| GET | `/api/v1/transactions/recent` | Get recent transactions |
| PUT | `/api/v1/transactions/{id}` | Update transaction |
| DELETE | `/api/v1/transactions/{id}` | Delete transaction |

### 💰 Budgets
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/budgets` | Create budget |
| GET | `/api/v1/budgets` | Get all budgets |
| GET | `/api/v1/budgets/{id}` | Get budget by ID |
| GET | `/api/v1/budgets/month` | Get budgets for month |
| GET | `/api/v1/budgets/{id}/status` | Check budget status |
| PUT | `/api/v1/budgets/{id}` | Update budget |
| DELETE | `/api/v1/budgets/{id}` | Delete budget |

### 🏷️ Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/categories` | Create category |
| GET | `/api/v1/categories` | Get all categories |
| GET | `/api/v1/categories/{id}` | Get category by ID |
| GET | `/api/v1/categories/type/{type}` | Get categories by type |
| PUT | `/api/v1/categories/{id}` | Update category |
| DELETE | `/api/v1/categories/{id}` | Delete category |

---

## 🔑 **Authentication**

All endpoints (except `/auth/register` and `/auth/login`) require JWT token in the `Authorization` header:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### Getting a Token

1. **Register** (if new user):
```
POST /api/v1/auth/register
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "defaultCurrency": "USD"
}
```

2. **Login**:
```
POST /api/v1/auth/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123"
}


Response:

{
    "success": true,
    "message": "User logged in successfully",
    "data": {
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "tokenType": "Bearer",
        "userId": 1,
        "email": "user@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "role": "USER"
    }
}
```

---

## 🗄️ **Database Schema**

### Tables
```
- **users** - User accounts
- **categories** - Transaction categories
- **transactions** - Income/expense records
- **budgets** - Budget limits and tracking
```
### Relationships
```
User (1) ──── (M) Transaction
User (1) ──── (M) Budget
User (1) ──── (M) Category
Category (1) ──── (M) Transaction
Category (1) ──── (M) Budget
```

---

## ⚠️ **Error Handling**

All errors are returned with consistent format:

```
{
    "success": false,
    "message": "Error description",
    "timestamp": "2025-11-03T14:30:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/api/v1/transactions"
}
```

### Common Status Codes
- **200** - OK
- **201** - Created
- **400** - Bad Request
- **401** - Unauthorized
- **404** - Not Found
- **500** - Internal Server Error

---

## 📝 **Example Usage**

### Create a Transaction
```
curl -X POST http://localhost:8080/api/v1/transactions \
    -H "Authorization: Bearer YOUR_JWT_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
    "amount": 50.00,
    "type": "EXPENSE",
    "description": "Coffee",
    "transactionDate": "2025-11-03",
    "categoryId": 1 }'
```

### Get All Transactions (with pagination)

```
curl -X GET "http://localhost:8080/api/v1/transactions?page=0&size=20&sort=transactionDate,desc" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Create a Budget
```
curl -X POST http://localhost:8080/api/v1/budgets \
    -H "Authorization: Bearer YOUR_JWT_TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
    "categoryId": 1,
    "limitAmount": 500.00,
    "month": "2025-11-01",
    "alertEnabled": true
}'
```

---

## 🔄 **Development Workflow**

1. Create feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit Pull Request

---

## 📄 **License**

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 👨‍💻 **Author**

**Maciej Hosaniak**
- GitHub: [@Macijke](https://github.com/macijke)
- Email: macijke@gmail.com

---



**Made with ❤️ by Macijke**