# 🚀 QUICK RUN GUIDE - E-Commerce Platform

## ⚠️ Important Note

The services have some dependencies that need to be configured before running:

1. **Eureka Server** - Not yet created (services try to register with it)
2. **Config Server** - Not yet created (services try to fetch config)
3. **MySQL Database** - Needs to be running

---

## 🔧 OPTION 1: Run Services Without Infrastructure (Recommended for Testing)

### Step 1: Disable Eureka & Config Server Temporarily

For each service, update `application.properties`:

**User Service** (`backend/user-service/src/main/resources/application.properties`):
```properties
# Add these lines to disable Eureka temporarily
eureka.client.enabled=false
spring.cloud.config.enabled=false
```

**Product Service** (`backend/product-service/src/main/resources/application.properties`):
```properties
# Add these lines
eureka.client.enabled=false
spring.cloud.config.enabled=false
```

### Step 2: Start MySQL

Make sure MySQL is running on your system.

### Step 3: Create Databases

```sql
CREATE DATABASE ecommerce_user;
CREATE DATABASE ecommerce_product;
CREATE DATABASE ecommerce_order;
```

### Step 4: Build & Run Services

**Terminal 1 - User Service:**
```bash
cd backend/user-service
mvn clean install -DskipTests
mvn spring-boot:run
```

**Terminal 2 - Product Service:**
```bash
cd backend/product-service
mvn clean install -DskipTests
mvn spring-boot:run
```

### Step 5: Access Swagger UI

- **User Service**: http://localhost:8081/swagger-ui.html
- **Product Service**: http://localhost:8083/swagger-ui.html

---

## 🔧 OPTION 2: Simplified POM (Remove Cloud Dependencies)

If you want to run without any cloud dependencies, update each service's `pom.xml`:

**Comment out these dependencies:**
```xml
<!-- Temporarily disable
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
-->
```

**Remove these annotations from Application class:**
```java
// @EnableDiscoveryClient  // Comment this out
```

---

## 🧪 OPTION 3: Test with Postman/cURL

### Register a User

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

### Create a Category

```bash
curl -X POST http://localhost:8083/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices and accessories",
    "displayOrder": 1
  }'
```

### Create a Product

```bash
curl -X POST http://localhost:8083/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Latest iPhone with A17 Pro chip",
    "sku": "IPH15PRO-001",
    "brand": "Apple",
    "price": 999.99,
    "discountPercentage": 10,
    "stockQuantity": 50,
    "categoryId": 1,
    "isFeatured": true
  }'
```

---

## 🐛 TROUBLESHOOTING

### Issue: Build Fails

**Solution 1**: Make sure Common Library is installed
```bash
cd backend/common-library
mvn clean install
```

**Solution 2**: Check Java version
```bash
java -version  # Should be Java 17
```

**Solution 3**: Clean Maven cache
```bash
mvn dependency:purge-local-repository
```

### Issue: Cannot Connect to Database

**Solution**: Check MySQL is running
```bash
# Windows
net start MySQL80

# Check connection
mysql -u root -p
```

### Issue: Port Already in Use

**Solution**: Kill the process using the port
```bash
# Windows - Find process on port 8081
netstat -ano | findstr :8081

# Kill process
taskkill /PID <PID> /F
```

### Issue: Eureka Connection Error

**Solution**: Disable Eureka as shown in Option 1 above

---

## ✅ RECOMMENDED APPROACH

**For immediate testing, use OPTION 1:**

1. Disable Eureka and Config Server in `application.properties`
2. Make sure MySQL is running
3. Create databases
4. Build and run User Service
5. Build and run Product Service
6. Test with Swagger UI

**This will let you test the core functionality without infrastructure services!**

---

## 📝 NEXT STEPS AFTER TESTING

Once you've tested the services:

1. Build infrastructure services (Eureka, Config Server, API Gateway)
2. Re-enable cloud dependencies
3. Run all services together
4. Test service-to-service communication

---

## 🎯 QUICK TEST CHECKLIST

- [ ] MySQL is running
- [ ] Databases created
- [ ] Common library built (`mvn clean install`)
- [ ] Eureka disabled in application.properties
- [ ] User Service running on 8081
- [ ] Product Service running on 8083
- [ ] Swagger UI accessible
- [ ] Can register a user
- [ ] Can login and get JWT token
- [ ] Can create categories
- [ ] Can create products

---

**Start with User Service first, then Product Service. Test each one before moving to the next!**

Good luck! 🚀
