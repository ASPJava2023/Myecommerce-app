package com.ecommerce.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * User Service - Main Application
 * Handles user authentication, authorization, and user management
 * 
 * Features:
 * - JWT Authentication
 * - Google OAuth2 Login
 * - User Registration & Login
 * - Password Reset
 * - Address Management
 * - Role-Based Access Control
 * 
 * @author E-Commerce Platform Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
