package com.ecommerce.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Product Service - Main Application
 * Handles product catalog, categories, and search
 * 
 * @author E-Commerce Platform Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
