# 🚀 QUICK RUN GUIDE - E-COMMERCE PLATFORM

**Last Updated**: January 20, 2026 - 23:22 IST

---

## ⚡ FASTEST WAY TO RUN THE PROJECT

### Prerequisites
- ✅ Java 17 installed
- ✅ Maven installed
- ✅ MySQL running
- ✅ Node.js installed (for frontend)

---

## 🎯 STEP-BY-STEP RUNNING GUIDE

### Step 1: Create Databases (1 min)

```sql
CREATE DATABASE ecommerce_user;
CREATE DATABASE ecommerce_product;
CREATE DATABASE ecommerce_order;
```

### Step 2: Build Common Library (2 min)

```bash
cd backend/common-library
mvn clean install
```

### Step 3: Run User Service (Port 8081)

**Terminal 1:**
```bash
cd backend/user-service
mvn spring-boot:run
```

**Wait for**: "Started UserServiceApplication"  
**Access**: http://localhost:8081/swagger-ui.html

### Step 4: Run Product Service (Port 8083)

**Terminal 2:**
```bash
cd backend/product-service
mvn spring-boot:run
```

**Wait for**: "Started ProductServiceApplication"  
**Access**: http://localhost:8083/swagger-ui.html

---

## 🧪 TEST THE SERVICES

### Test User Service

**1. Register a User:**
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "Password@123",
    "confirmPassword": "Password@123",
    "phoneNumber": "+1234567890"
  }'
```

**2. Login:**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "Password@123"
  }'
```

**Copy the `accessToken` from response!**

### Test Product Service

**1. Create Category:**
```bash
curl -X POST http://localhost:8083/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices and accessories",
    "displayOrder": 1
  }'
```

**2. Create Product:**
```bash
curl -X POST http://localhost:8083/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Latest iPhone with A17 Pro chip",
    "sku": "IPH15PRO-001",
    "brand": "Apple",
    "price": 999.99,
    "stockQuantity": 50,
    "categoryId": 1,
    "isFeatured": true
  }'
```

**3. Get All Products:**
```bash
curl http://localhost:8083/api/products
```

---

## 🌐 ACCESS POINTS

### Swagger UI (API Documentation)
- **User Service**: http://localhost:8081/swagger-ui.html
- **Product Service**: http://localhost:8083/swagger-ui.html

### Actuator (Health Check)
- **User Service**: http://localhost:8081/actuator/health
- **Product Service**: http://localhost:8083/actuator/health

---

## 🐛 TROUBLESHOOTING

### Issue: Port Already in Use

**Solution:**
```bash
# Windows - Find process on port
netstat -ano | findstr :8081

# Kill process
taskkill /PID <PID> /F
```

### Issue: Cannot Connect to Database

**Solution:**
```bash
# Check MySQL is running
mysql -u root -p

# Verify databases exist
SHOW DATABASES;
```

### Issue: Build Fails

**Solution:**
```bash
# Rebuild common library
cd backend/common-library
mvn clean install -U

# Clear Maven cache
mvn dependency:purge-local-repository
```

---

## ✅ SUCCESS CHECKLIST

- [ ] MySQL is running
- [ ] Databases created (ecommerce_user, ecommerce_product)
- [ ] Common library built successfully
- [ ] User Service running on 8081
- [ ] Product Service running on 8083
- [ ] Swagger UI accessible
- [ ] Can register a user
- [ ] Can login and get JWT token
- [ ] Can create categories
- [ ] Can create products

---

## 🎯 WHAT'S RUNNING

When both services are running, you have:

✅ **User Service (8081)**
- User registration
- JWT login
- User management
- Role-based access

✅ **Product Service (8083)**
- Product CRUD
- Category management
- Search & filter
- Pagination

---

## 🚀 NEXT: RUN MORE SERVICES

To run additional services, see:
- `ORDER_SERVICE_IMPLEMENTATION.md` - Order Service
- `PHASE2_COMPLETION_GUIDE.md` - Seller, Admin, Notification
- `COMPLETE_IMPLEMENTATION_GUIDE.md` - Infrastructure

---

**You're ready to test the platform!** 🎉
