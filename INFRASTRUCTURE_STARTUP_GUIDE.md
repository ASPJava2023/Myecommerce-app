# 🚀 Infrastructure Services - Quick Startup Guide

## ⚡ Quick Start (5 Services in Order)

### Prerequisites
- MySQL running on localhost:3306
- Java 17 installed
- Maven installed

---

## 📋 Startup Sequence

Open **5 separate terminal windows** and run these commands in order:

### Terminal 1: Eureka Server (START FIRST)
```bash
cd e:\Dec_Month_Java_Learning\ecommerce-app\backend\eureka-server
mvn spring-boot:run
```
✅ Wait for: "Started EurekaServerApplication"  
🌐 Dashboard: http://localhost:8761

---

### Terminal 2: Config Server (START SECOND)
```bash
cd e:\Dec_Month_Java_Learning\ecommerce-app\backend\config-server
mvn spring-boot:run
```
✅ Wait for: "Started ConfigServerApplication"  
🔍 Health: http://localhost:8888/actuator/health

---

### Terminal 3: API Gateway (START THIRD)
```bash
cd e:\Dec_Month_Java_Learning\ecommerce-app\backend\api-gateway
mvn spring-boot:run
```
✅ Wait for: "Started ApiGatewayApplication"  
🔍 Health: http://localhost:8080/actuator/health

---

### Terminal 4: User Service (START FOURTH)
```bash
cd e:\Dec_Month_Java_Learning\ecommerce-app\backend\user-service
mvn spring-boot:run
```
✅ Wait for: "Started UserServiceApplication"  
🔍 Direct: http://localhost:8081/actuator/health

---

### Terminal 5: Product Service (START FIFTH)
```bash
cd e:\Dec_Month_Java_Learning\ecommerce-app\backend\product-service
mvn spring-boot:run
```
✅ Wait for: "Started ProductServiceApplication"  
🔍 Direct: http://localhost:8083/actuator/health

---

## ✅ Verification Checklist

After all services start, verify:

### 1. Eureka Dashboard
- [ ] Open http://localhost:8761
- [ ] Verify 3 services registered:
  - API-GATEWAY
  - USER-SERVICE
  - PRODUCT-SERVICE

### 2. Direct Service Access
- [ ] User Service: http://localhost:8081/actuator/health
- [ ] Product Service: http://localhost:8083/actuator/health

### 3. Gateway Routing
- [ ] Via Gateway to User: http://localhost:8080/api/auth/health
- [ ] Via Gateway to Product: http://localhost:8080/api/products

---

## 🎯 Expected Results

| Service | Port | Status Indicator |
|---------|------|------------------|
| Eureka | 8761 | Dashboard shows 3 services |
| Config | 8888 | {"status":"UP"} |
| Gateway | 8080 | {"status":"UP"} + registered in Eureka |
| User | 8081 | {"status":"UP"} + registered in Eureka |
| Product | 8083 | {"status":"UP"} + registered in Eureka |

---

## 🔧 Troubleshooting

### Service won't start
- Check if port is already in use
- Verify MySQL is running
- Check Java version: `java -version` (should be 17)

### Service not registering with Eureka
- Ensure Eureka started first
- Wait 30 seconds for registration
- Check application.properties has `eureka.client.enabled=true`

### Gateway routing fails
- Verify service is registered in Eureka dashboard
- Check Gateway logs for routing errors
- Ensure service name matches in Gateway routes

---

## 📊 Port Reference

| Service | Port |
|---------|------|
| Eureka Server | 8761 |
| Config Server | 8888 |
| API Gateway | 8080 |
| User Service | 8081 |
| Product Service | 8083 |

---

**Ready to Start!** 🎉
