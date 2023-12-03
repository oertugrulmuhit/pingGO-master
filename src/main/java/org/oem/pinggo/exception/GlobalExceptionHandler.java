package org.oem.pinggo.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({RequiredSellerException.class})
    public ResponseEntity<?> handleRequiredSellerException(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(OrderStatusNoEligibleForEditException.class)
    public ResponseEntity<Object> handleOrderStatusNoEligibleForEditException(OrderStatusNoEligibleForEditException orderStatusNoEligibleForEditException, WebRequest request) {
        return buildErrorResponse(orderStatusNoEligibleForEditException, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(HttpServletRequest request, Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ex.getMessage());
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementFoundException.class)
    public ResponseEntity<Object> handleNoSuchElementFoundException(NoSuchElementFoundException itemNotFoundException, WebRequest request) {
        return buildErrorResponse(itemNotFoundException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ItemOwnerException.class)
    public ResponseEntity<Object> handleItemOwnerException(ItemOwnerException itemOwnerException, WebRequest request) {
        return buildErrorResponse(itemOwnerException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InsufficientResourceException.class)
    public ResponseEntity<Object> handleInsufficientResourceException(InsufficientResourceException insufficientResourceException, WebRequest request) {
        return buildErrorResponse(insufficientResourceException, HttpStatus.NOT_ACCEPTABLE, request);
    }

    @ExceptionHandler(UserNotFoundWithIdException.class)
    public ResponseEntity<Object> handleUserNotFoundWithIdException(UserNotFoundWithIdException userNotFoundWithIdException, WebRequest request) {
        return buildErrorResponse(userNotFoundWithIdException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserNotFoundWithEmailException.class)
    public ResponseEntity<Object> handleUserNotFoundWithEmailException(UserNotFoundWithEmailException userNotFoundWithEmailException, WebRequest request) {
        return buildErrorResponse(userNotFoundWithEmailException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(TimeoutVerificationTokenException.class)
    public ResponseEntity<Object> handleTimeoutVerificationTokenException(TimeoutVerificationTokenException timeoutVerificationTokenException, WebRequest request) {
        return buildErrorResponse(timeoutVerificationTokenException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InvalidVerificationTokenException.class)
    public ResponseEntity<Object> handleInvalidVerificationTokenException(InvalidVerificationTokenException invalidVerificationTokenException, WebRequest request) {
        return buildErrorResponse(invalidVerificationTokenException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        return buildErrorResponse(exception, "..." + exception.getMessage() + "...", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus, WebRequest request) {
        return ResponseEntity.status(httpStatus).body(message);
    }
}