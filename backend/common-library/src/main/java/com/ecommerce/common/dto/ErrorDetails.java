package com.ecommerce.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Error details for API responses
 * Provides structured error information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {

    /**
     * Error code for programmatic handling
     */
    private String errorCode;

    /**
     * Detailed error message
     */
    private String message;

    /**
     * Field-level validation errors
     */
    private Map<String, String> fieldErrors;

    /**
     * List of error messages
     */
    private List<String> errors;

    /**
     * Stack trace (only in development mode)
     */
    private String stackTrace;

    /**
     * HTTP status code
     */
    private Integer status;

    /**
     * Request path that caused the error
     */
    private String path;
}
