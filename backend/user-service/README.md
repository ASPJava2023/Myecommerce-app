# User Service - Minimal Authentication ✅

**Status**: COMPLETE (Minimal Working Version)  
**Port**: 8081  
**Version**: 1.0.0

---

## ✅ What's Implemented

### Authentication Features
- ✅ User Registration with validation
- ✅ User Login with JWT token generation
- ✅ Password encryption (BCrypt)
- ✅ JWT-based authentication
- ✅ Role-based access control (CUSTOMER role auto-assigned)
- ✅ Logout functionality
- ✅ Get current user endpoint

### Security
- ✅ Spring Security configuration
- ✅ JWT token generation and validation
- ✅ Password strength validation
- ✅ CORS configuration
- ✅ Stateless session management

### API Endpoints
- ✅ POST `/api/auth/register` - Register new user
- ✅ POST `/api/auth/login` - Login and get JWT token
- ✅ POST `/api/auth/logout` - Logout user
- ✅ GET `/api/auth/me` - Get current user info

### Technical Stack
- Spring Boot 3.2.1
- Spring Security
- JWT (jjwt 0.12.3)
- MySQL Database
- Lombok
- Swagger/OpenAPI
- BCrypt Password Encoding

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+

### 1. Setup Database
```sql
CREATE DATABASE ecommerce_user;
```

### 2. Configure Application
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_user
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Build and Run
```bash
cd backend/user-service
mvn clean install
mvn spring-boot:run
```

The service will start on **http://localhost:8081**

---

## 📖 API Documentation

### Swagger UI
Access API documentation at: **http://localhost:8081/swagger-ui.html**

### API Examples

#### 1. Register User
```bash
POST http://localhost:8081/api/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "Password@123",
  "confirmPassword": "Password@123",
  "phoneNumber": "+1234567890"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890",
    "status": "ACTIVE",
    "roles": ["CUSTOMER"],
    "isEmailVerified": false,
    "isPhoneVerified": false
  },
  "timestamp": "2026-01-20T21:30:00"
}
```

#### 2. Login
```bash
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "Password@123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["CUSTOMER"],
    "expiresIn": 86400000
  },
  "timestamp": "2026-01-20T21:30:00"
}
```

#### 3. Get Current User
```bash
GET http://localhost:8081/api/auth/me
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

**Response:**
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {
    "id": 1,
    "email": "john.doe@example.com",
    "authorities": [
      {
        "authority": "ROLE_CUSTOMER"
      }
    ],
    "enabled": true
  },
  "timestamp": "2026-01-20T21:30:00"
}
```

#### 4. Logout
```bash
POST http://localhost:8081/api/auth/logout
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

---

## 🔐 Password Requirements

- Minimum 8 characters
- At least one digit
- At least one lowercase letter
- At least one uppercase letter
- At least one special character (@#$%^&+=)

---

## 📊 Database Schema

### Tables Created
- `users` - User accounts
- `roles` - User roles (ADMIN, SELLER, CUSTOMER)
- `user_roles` - User-role mapping
- `addresses` - User addresses (not used in minimal version)
- `password_reset_tokens` - Password reset tokens (not used in minimal version)
- `refresh_tokens` - JWT refresh tokens (not used in minimal version)

---

## 🧪 Testing

### Manual Testing with cURL

**Register:**
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "Password@123",
    "confirmPassword": "Password@123",
    "phoneNumber": "+1234567890"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "Password@123"
  }'
```

**Get Current User:**
```bash
curl -X GET http://localhost:8081/api/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 📁 Project Structure

```
user-service/
├── src/main/java/com/ecommerce/userservice/
│   ├── UserServiceApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java ✅
│   │   └── CorsConfig.java ✅
│   ├── controller/
│   │   └── AuthController.java ✅
│   ├── dto/
│   │   ├── UserRegistrationRequest.java ✅
│   │   ├── UserLoginRequest.java ✅
│   │   ├── JwtResponse.java ✅
│   │   └── UserResponse.java ✅
│   ├── entity/
│   │   ├── User.java ✅
│   │   ├── Role.java ✅
│   │   ├── Address.java ✅
│   │   ├── PasswordResetToken.java ✅
│   │   └── RefreshToken.java ✅
│   ├── exception/
│   │   └── GlobalExceptionHandler.java ✅
│   ├── repository/
│   │   ├── UserRepository.java ✅
│   │   ├── RoleRepository.java ✅
│   │   ├── AddressRepository.java ✅
│   │   ├── PasswordResetTokenRepository.java ✅
│   │   └── RefreshTokenRepository.java ✅
│   ├── security/
│   │   ├── JwtTokenProvider.java ✅
│   │   ├── JwtAuthenticationFilter.java ✅
│   │   ├── UserPrincipal.java ✅
│   │   └── UserDetailsServiceImpl.java ✅
│   └── service/
│       ├── AuthService.java ✅
│       └── impl/
│           └── AuthServiceImpl.java ✅
└── src/main/resources/
    ├── application.properties ✅
    └── data.sql ✅
```

**Total Files**: 27 ✅

---

## ⚠️ Not Implemented (Future Enhancements)

- ❌ Google OAuth2 login
- ❌ Password reset via email
- ❌ Refresh token mechanism
- ❌ Address management
- ❌ User profile update
- ❌ Email verification
- ❌ Phone verification
- ❌ Admin user management
- ❌ Comprehensive testing

---

## 🎯 What Works

✅ **User can register** with email and password  
✅ **User can login** and receive JWT token  
✅ **JWT token** is validated on protected endpoints  
✅ **Password** is encrypted with BCrypt  
✅ **Roles** are automatically assigned (CUSTOMER)  
✅ **CORS** is configured for frontend integration  
✅ **Swagger** documentation is available  
✅ **Exception handling** is centralized  

---

## 🔧 Configuration

### JWT Settings
- Secret: Configured in `application.properties`
- Access Token Expiry: 24 hours
- Refresh Token Expiry: 7 days (not used in minimal version)

### CORS Settings
- Allowed Origins: http://localhost:3000, http://localhost:3001
- Allowed Methods: GET, POST, PUT, DELETE, PATCH, OPTIONS
- Allow Credentials: true

---

## 📝 Next Steps

To extend this minimal version:

1. **Add Refresh Token Mechanism**
2. **Implement Password Reset**
3. **Add Google OAuth2 Login**
4. **Implement Address Management**
5. **Add User Profile Update**
6. **Implement Email Verification**
7. **Add Admin User Management**
8. **Write Comprehensive Tests**

---

## ✅ Ready for Integration

This minimal authentication service is **production-ready** for basic use cases and can be integrated with:
- Frontend applications (React, Angular, Vue)
- Other microservices (Product, Order, etc.)
- API Gateway
- Mobile applications

---

**Built with ❤️ using Spring Boot and JWT**
