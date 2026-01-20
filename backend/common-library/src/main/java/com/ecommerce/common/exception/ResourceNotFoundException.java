package com.ecommerce.common.exception;

/**
 * Exception thrown when a requested resource is not found
 * HTTP Status: 404 NOT FOUND
 */
public class ResourceNotFoundException extends EcommerceException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue),
                "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id),
                "RESOURCE_NOT_FOUND");
    }
}
