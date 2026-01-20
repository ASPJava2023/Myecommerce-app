# 🚀 Quick Start Guide - E-Commerce Platform

## ✅ What's Ready to Use

### User Service - FULLY FUNCTIONAL ✅
**Port**: 8081  
**Database**: ecommerce_user  
**Status**: Production-ready (minimal version)

---

## 🏃 Quick Start (5 Minutes)

### 1. Setup Database
```sql
CREATE DATABASE ecommerce_user;
```

### 2. Configure Database
Edit `backend/user-service/src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Build & Run
```bash
cd backend/user-service
mvn clean install
mvn spring-boot:run
```

### 4. Test It!
Open browser: **http://localhost:8081/swagger-ui.html**

---

## 📝 Test the API

### Register a User
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "Password@123",
    "confirmPassword": "Password@123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "Password@123"
  }'
```

**Copy the `accessToken` from the response!**

### Get Current User (Protected Endpoint)
```bash
curl -X GET http://localhost:8081/api/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## 🎯 What Works

✅ User Registration  
✅ User Login  
✅ JWT Token Generation  
✅ Password Encryption  
✅ Protected Endpoints  
✅ Role-Based Access  
✅ Swagger Documentation  
✅ Exception Handling  

---

## 📊 Project Status

| Service | Status | Completion |
|---------|--------|------------|
| Common Library | ✅ Built | 100% |
| User Service | ✅ Working | 100% (Minimal) |
| Product Service | 🚧 Started | 10% |
| Order Service | ⏳ Pending | 0% |
| Seller Service | ⏳ Pending | 0% |
| Admin Service | ⏳ Pending | 0% |
| Notification Service | ⏳ Pending | 0% |

---

## 🔧 Troubleshooting

### Port Already in Use
```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8081
kill -9 <PID>
```

### Database Connection Error
- Check MySQL is running
- Verify database exists
- Check username/password in application.properties

### Build Errors
```bash
# Clean and rebuild
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

---

## 📖 Documentation

- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **API Docs**: http://localhost:8081/api-docs
- **Actuator**: http://localhost:8081/actuator
- **Health Check**: http://localhost:8081/actuator/health

---

## 🎓 Password Requirements

- Minimum 8 characters
- At least one digit
- At least one lowercase letter
- At least one uppercase letter
- At least one special character (@#$%^&+=)

Example: `Password@123`

---

## 📁 Key Files

```
user-service/
├── pom.xml                          # Maven configuration
├── README.md                        # Detailed documentation
├── COMPLETION_SUMMARY.md            # What's implemented
└── src/main/
    ├── java/com/ecommerce/userservice/
    │   ├── UserServiceApplication.java    # Main class
    │   ├── controller/
    │   │   └── AuthController.java        # API endpoints
    │   ├── service/impl/
    │   │   └── AuthServiceImpl.java       # Business logic
    │   └── security/
    │       ├── SecurityConfig.java        # Security setup
    │       └── JwtTokenProvider.java      # JWT handling
    └── resources/
        ├── application.properties         # Configuration
        └── data.sql                       # Initial data
```

---

## 🚀 Next Steps

1. **Test User Service** ✅ (You are here!)
2. **Complete Product Service** 📦
3. **Build Order Service** 🛒
4. **Add Infrastructure** 🏗️
5. **Create Frontend** 💻

---

## 💡 Pro Tips

1. **Use Swagger** - Easiest way to test APIs
2. **Save JWT Token** - You'll need it for protected endpoints
3. **Check Logs** - Helpful for debugging
4. **Use Postman** - Better for complex testing
5. **Read README** - Each service has detailed docs

---

## 🎉 You're All Set!

The User Service is ready to use. Start testing and when you're ready, we can continue building the Product Service!

---

**Happy Coding!** 🚀
