package com.ecommerce.common.exception;

/**
 * Exception thrown when a resource already exists
 * HTTP Status: 409 CONFLICT
 */
public class ResourceAlreadyExistsException extends EcommerceException {

    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue),
                "RESOURCE_ALREADY_EXISTS");
    }

    public ResourceAlreadyExistsException(String message) {
        super(message, "RESOURCE_ALREADY_EXISTS");
    }
}
