package com.hinchmart.exception;

import com.hinchmart.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn(">>> [SERVICE LAYER 404] ResourceNotFoundException: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("NOT_FOUND", ex.getMessage(), null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
        String code = "BAD_REQUEST";
        String msg = ex.getMessage();
        if (msg != null && msg.toUpperCase().contains("OTP")) {
            code = "INVALID_OTP";
        } else if (msg != null && msg.toUpperCase().contains("TOKEN")) {
            code = "INVALID_TOKEN";
        }
        log.warn(">>> [SERVICE/CONTROLLER LAYER 400] BadRequestException ({}): {}", code, msg);
        return new ResponseEntity<>(ApiResponse.error(code, msg, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex) {
        log.warn(">>> [SECURITY/SERVICE LAYER 401] UnauthorizedException: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("UNAUTHORIZED", ex.getMessage(), null), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        log.warn(">>> [AUTH 401] BadCredentialsException: Invalid email/phone or password");
        return new ResponseEntity<>(ApiResponse.error("INVALID_CREDENTIALS", "Invalid email/phone or password", null), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        log.warn(">>> [SECURITY 403] AccessDeniedException: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("ACCESS_DENIED", "Access denied: You do not have permission to perform this action", null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn(">>> [CONTROLLER VALIDATION 400] Validation failed for fields: {}", errors);
        return new ResponseEntity<>(ApiResponse.error("VALIDATION_ERROR", "Request validation failed", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParams(MissingServletRequestParameterException ex) {
        log.warn(">>> [CONTROLLER LAYER 400] Missing required request parameter '{}'", ex.getParameterName());
        return new ResponseEntity<>(ApiResponse.error("MISSING_PARAMETER", "Required parameter '" + ex.getParameterName() + "' is missing", null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn(">>> [CONTROLLER LAYER 400] Type mismatch for parameter '{}': expected {}", ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "valid type");
        return new ResponseEntity<>(ApiResponse.error("TYPE_MISMATCH", "Parameter '" + ex.getName() + "' has invalid type or format", null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn(">>> [CONTROLLER LAYER 400] Malformed JSON request body: {}", ex.getMessage());
        return new ResponseEntity<>(ApiResponse.error("MALFORMED_JSON", "Malformed or unreadable JSON request body", null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn(">>> [CONTROLLER LAYER 405] Method '{}' not supported for this URL", ex.getMethod());
        return new ResponseEntity<>(ApiResponse.error("METHOD_NOT_ALLOWED", "HTTP method " + ex.getMethod() + " is not supported for this endpoint", null), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.error(">>> [DATABASE/REPO LAYER 409] DataIntegrityViolationException: {}", ex.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(ApiResponse.error("DATA_INTEGRITY_ERROR", "Database constraint violation: " + ex.getMostSpecificCause().getMessage(), null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        log.error(">>> [SERVER ERROR 500] Unexpected exception occurred: ", ex);
        return new ResponseEntity<>(ApiResponse.error("INTERNAL_SERVER_ERROR", "An unexpected error occurred: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
