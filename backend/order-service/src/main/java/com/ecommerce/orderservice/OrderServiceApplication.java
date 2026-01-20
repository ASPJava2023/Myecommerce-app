package com.ecommerce.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Order Service - Main Application
 * Handles shopping cart and order management
 * 
 * @author E-Commerce Platform Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
