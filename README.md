# 🛒 Enterprise E-Commerce Platform

## 📋 Project Overview

A scalable, secure, production-grade e-commerce application built with Java Spring Boot microservices architecture, React frontend, and AWS cloud deployment.

### 🎯 Key Features

- **Multi-Vendor Marketplace**: Support for multiple sellers
- **Secure Authentication**: JWT + Google OAuth2
- **Payment Integration**: Real Razorpay payment gateway
- **Multi-Currency Support**: Dynamic currency conversion
- **Role-Based Access**: Admin, Seller, and Customer roles
- **Cloud-Native**: AWS-ready with Docker containerization
- **High Test Coverage**: 90%+ code coverage

## 🏗️ Architecture

### Microservices Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        API Gateway                           │
│                    (Port: 8080)                              │
└─────────────────────────────────────────────────────────────┘
                            │
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│ User Service │    │Product Service│   │Order Service │
│  (Port: 8081)│    │  (Port: 8083) │   │ (Port: 8084) │
└──────────────┘    └──────────────┘    └──────────────┘
        │                   │                   │
        ▼                   ▼                   ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│Seller Service│    │ Admin Service│    │Notification  │
│  (Port: 8082)│    │  (Port: 8086) │   │   Service    │
└──────────────┘    └──────────────┘    │ (Port: 8085) │
                                        └──────────────┘
                            │
                            ▼
                ┌──────────────────────┐
                │  Discovery Server    │
                │   (Eureka - 8761)    │
                └──────────────────────┘
                            │
                            ▼
                ┌──────────────────────┐
                │   Config Server      │
                │     (Port: 8888)     │
                └──────────────────────┘
```

## 🛠️ Technology Stack

### Backend
- **Java**: 17
- **Spring Boot**: 3.x
- **Spring Cloud**: Config Server, Eureka, Gateway
- **Spring Security**: JWT + OAuth2
- **Database**: MySQL (AWS RDS)
- **ORM**: Spring Data JPA + Hibernate
- **Build Tool**: Maven (multi-module)
- **Documentation**: OpenAPI/Swagger 3
- **Monitoring**: Spring Boot Actuator
- **Resilience**: Resilience4j
- **Reporting**: Apache POI, OpenCSV

### Frontend
- **React**: 18
- **HTTP Client**: Axios
- **Routing**: React Router
- **State Management**: Context API / Redux

### DevOps & Cloud
- **Containerization**: Docker
- **CI/CD**: GitHub Actions
- **Cloud Provider**: AWS (EC2, RDS, S3, ALB)
- **Monitoring**: CloudWatch

## 📂 Project Structure

```
ecommerce-app/
│
├── backend/
│   ├── config-server/          # Centralized configuration
│   ├── discovery-server/       # Eureka service registry
│   ├── api-gateway/            # API Gateway with auth
│   ├── user-service/           # User management & auth
│   ├── admin-service/          # Admin operations
│   ├── product-service/        # Product catalog
│   ├── order-service/          # Order & payment
│   ├── notification-service/   # Email/SMS notifications
│   ├── seller-service/         # Seller management
│   └── common-library/         # Shared DTOs & utilities
│
├── frontend/
│   ├── customer-ui/            # Customer-facing app
│   └── admin-ui/               # Admin dashboard
│
├── devops/
│   ├── docker/                 # Dockerfiles
│   ├── github-actions/         # CI/CD workflows
│   └── aws/                    # AWS deployment scripts
│
├── database/
│   └── schema.sql              # Database schema
│
└── docs/
    ├── architecture/           # Architecture diagrams
    └── api/                    # API documentation
```

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Node.js 18+
- Maven 3.8+
- Docker & Docker Compose
- MySQL 8.0+
- AWS Account (for deployment)

### Local Development Setup

#### 1. Clone the Repository

```bash
git clone <repository-url>
cd ecommerce-app
```

#### 2. Database Setup

```bash
# Create MySQL database
mysql -u root -p
CREATE DATABASE ecommerce_user;
CREATE DATABASE ecommerce_product;
CREATE DATABASE ecommerce_order;
CREATE DATABASE ecommerce_seller;
CREATE DATABASE ecommerce_admin;
```

#### 3. Configure Environment Variables

Create `.env` file in each service directory:

```properties
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ecommerce_user
DB_USERNAME=root
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000

# OAuth2 Google
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# Razorpay
RAZORPAY_KEY_ID=your_razorpay_key
RAZORPAY_KEY_SECRET=your_razorpay_secret

# AWS
AWS_ACCESS_KEY=your_aws_access_key
AWS_SECRET_KEY=your_aws_secret_key
AWS_S3_BUCKET=your_s3_bucket_name
```

#### 4. Build Backend Services

```bash
cd backend
mvn clean install -DskipTests
```

#### 5. Start Services (in order)

```bash
# 1. Config Server
cd config-server
mvn spring-boot:run

# 2. Discovery Server
cd discovery-server
mvn spring-boot:run

# 3. API Gateway
cd api-gateway
mvn spring-boot:run

# 4. Microservices (can run in parallel)
cd user-service && mvn spring-boot:run
cd seller-service && mvn spring-boot:run
cd product-service && mvn spring-boot:run
cd order-service && mvn spring-boot:run
cd admin-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

#### 6. Start Frontend

```bash
# Customer UI
cd frontend/customer-ui
npm install
npm start

# Admin UI
cd frontend/admin-ui
npm install
npm start
```

### Using Docker Compose

```bash
docker-compose up -d
```

## 🔐 Authentication Flow

### JWT Authentication

1. User registers/logs in via `/api/auth/login`
2. Server validates credentials
3. JWT token generated and returned
4. Client stores token (localStorage/cookie)
5. Token sent in `Authorization: Bearer <token>` header
6. API Gateway validates token
7. Request forwarded to microservice

### Google OAuth2 Flow

1. User clicks "Login with Google"
2. Redirected to Google consent screen
3. Google returns authorization code
4. Backend exchanges code for user info
5. User created/updated in database
6. JWT token generated and returned

## 📊 Database Schema

### Core Tables

- **users**: User accounts
- **roles**: User roles (ADMIN, SELLER, CUSTOMER)
- **user_roles**: User-role mapping
- **sellers**: Seller profiles
- **products**: Product catalog
- **categories**: Product categories
- **product_prices**: Multi-currency pricing
- **orders**: Order records
- **order_items**: Order line items
- **payments**: Payment transactions
- **addresses**: User addresses
- **currencies**: Supported currencies

See `database/schema.sql` for complete schema.

## 🧪 Testing

### Run All Tests

```bash
mvn clean test
```

### Run Tests with Coverage

```bash
mvn clean test jacoco:report
```

Coverage reports available at: `target/site/jacoco/index.html`

### Test Categories

- **Unit Tests**: Service layer logic
- **Integration Tests**: API endpoints with Testcontainers
- **Repository Tests**: Database operations

## 📖 API Documentation

### Swagger UI

Access API documentation at:

- **API Gateway**: http://localhost:8080/swagger-ui.html
- **User Service**: http://localhost:8081/swagger-ui.html
- **Product Service**: http://localhost:8083/swagger-ui.html
- **Order Service**: http://localhost:8084/swagger-ui.html

### Key Endpoints

#### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - JWT login
- `GET /api/auth/oauth2/google` - Google OAuth2

#### Products
- `GET /api/products` - List products (paginated)
- `GET /api/products/{id}` - Get product details
- `POST /api/products` - Create product (SELLER)

#### Orders
- `POST /api/orders` - Create order
- `GET /api/orders/{id}` - Get order details
- `POST /api/orders/{id}/payment` - Process payment

#### Admin
- `GET /api/admin/users` - List users
- `POST /api/admin/categories` - Create category
- `GET /api/admin/reports/sales` - Sales report

## 🌐 AWS Deployment

### Architecture

```
┌─────────────────────────────────────────┐
│     Application Load Balancer (ALB)     │
└─────────────────────────────────────────┘
                    │
        ┌───────────┴───────────┐
        ▼                       ▼
┌──────────────┐        ┌──────────────┐
│   EC2 (AZ-1) │        │   EC2 (AZ-2) │
│   Docker     │        │   Docker     │
└──────────────┘        └──────────────┘
        │                       │
        └───────────┬───────────┘
                    ▼
            ┌──────────────┐
            │   RDS MySQL  │
            │ (Multi-AZ)   │
            └──────────────┘
```

### Deployment Steps

1. **Setup AWS Infrastructure**

```bash
cd devops/aws
./setup-infrastructure.sh
```

2. **Build Docker Images**

```bash
cd devops/docker
./build-all.sh
```

3. **Push to ECR**

```bash
./push-to-ecr.sh
```

4. **Deploy to EC2**

```bash
./deploy-to-ec2.sh
```

### Environment Configuration

- **Dev**: `application-dev.properties`
- **QA**: `application-qa.properties`
- **Prod**: `application-prod.properties`

Secrets managed via **AWS Secrets Manager**.

## 📈 Monitoring & Observability

### Spring Boot Actuator

All services expose actuator endpoints:

- `/actuator/health` - Health check
- `/actuator/metrics` - Application metrics
- `/actuator/env` - Environment properties
- `/actuator/beans` - Spring beans

### Logging

- **Framework**: SLF4J + Logback
- **Correlation ID**: Tracked across services
- **Log Levels**: Configurable per environment

### Distributed Tracing

- **Zipkin**: Ready for integration
- **Correlation IDs**: Propagated via headers

## 🔒 Security Best Practices

- ✅ Passwords encrypted with BCrypt
- ✅ JWT with secure secret keys
- ✅ HTTPS enforced in production
- ✅ SQL injection prevention (JPA)
- ✅ XSS protection
- ✅ CORS configured
- ✅ Rate limiting on API Gateway
- ✅ Input validation on all DTOs

## 🎨 Design Patterns Used

- **Strategy Pattern**: Notification channels (Email/SMS)
- **Factory Pattern**: Payment gateway initialization
- **Repository Pattern**: Data access layer
- **DTO Pattern**: API data transfer
- **Builder Pattern**: Complex object creation
- **Singleton Pattern**: Configuration beans

## 📝 Code Quality

### SOLID Principles

- **S**ingle Responsibility: Each class has one purpose
- **O**pen/Closed: Open for extension, closed for modification
- **L**iskov Substitution: Subtypes are substitutable
- **I**nterface Segregation: Focused interfaces
- **D**ependency Inversion: Depend on abstractions

### Code Standards

- Clean code practices
- Meaningful variable names
- JavaDocs for public APIs
- No hardcoded values
- Centralized exception handling

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Team

- **Architecture**: Enterprise Architecture Team
- **Backend**: Java/Spring Boot Team
- **Frontend**: React Team
- **DevOps**: Cloud Infrastructure Team

## 📞 Support

For issues and questions:
- Create GitHub issue
- Email: support@ecommerce.com

---

**Built with ❤️ using Spring Boot, React, and AWS**
