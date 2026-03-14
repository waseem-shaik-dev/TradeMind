package com.trademind.auth.exceptionHandler;

import com.trademind.auth.dto.error.ApiError;
import com.trademind.auth.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // ===============================
    // VALIDATION ERRORS
    // ===============================

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(
                        err.getField(),
                        err.getDefaultMessage()));

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                errors.toString(),
                request.getDescription(false),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // ===============================
    // ACCESS TOKEN EXPIRED
    // ===============================

    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<ApiError> handleAccessTokenExpired(
            AccessTokenExpiredException ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                "TOKEN_EXPIRED",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ===============================
    // INVALID SIGNATURE
    // ===============================

    @ExceptionHandler(InvalidJwtSignatureException.class)
    public ResponseEntity<ApiError> handleInvalidSignature(
            InvalidJwtSignatureException ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                "INVALID_TOKEN_SIGNATURE",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ===============================
    // MALFORMED JWT
    // ===============================

    @ExceptionHandler(MalformedJwtExceptionCustom.class)
    public ResponseEntity<ApiError> handleMalformedJwt(
            MalformedJwtExceptionCustom ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                "MALFORMED_TOKEN",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ===============================
    // REFRESH TOKEN EXPIRED
    // ===============================

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ApiError> handleRefreshToken(
            RefreshTokenException ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                "INVALID_REFRESH_TOKEN",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TokenReuseException.class)
    public ResponseEntity<ApiError> handleTokenReuse(
            TokenReuseException ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                "TOKEN_REUSE_DETECTED",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ===============================
    // USER NOT FOUND
    // ===============================

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ===============================
    // INVALID CREDENTIALS
    // ===============================

    @ExceptionHandler({
            InvalidCredentialsException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ApiError> handleBadCredentials(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                "BAD_CREDENTIALS",
                "Invalid username or password",
                request.getRequestURI()
        );
    }

    // ===============================
    // FORBIDDEN
    // ===============================

    @ExceptionHandler({
            AccessDeniedException.class,
            AccessDeniedOperationException.class
    })
    public ResponseEntity<ApiError> handleForbidden(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.FORBIDDEN,
                "ACCESS_DENIED",
                "You do not have permission",
                request.getRequestURI()
        );
    }

    // ===============================
    // DATA INTEGRITY
    // ===============================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.CONFLICT,
                "DATA_INTEGRITY_VIOLATION",
                "Duplicate or invalid data",
                request.getRequestURI()
        );
    }

    // ===============================
    // METHOD NOT ALLOWED
    // ===============================

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ApiError error = new ApiError(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "METHOD_NOT_ALLOWED",
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // ===============================
    // GENERIC FALLBACK
    // ===============================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Unexpected error occurred",
                request.getRequestURI()
        );
    }

    // ===============================
    // COMMON BUILDER
    // ===============================

    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String errorCode,
            String message,
            String path
    ) {

        ApiError apiError = new ApiError(
                status.value(),
                errorCode,
                message,
                path,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, status);
    }
}
