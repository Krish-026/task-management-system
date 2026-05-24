package com.taskmanager.system.exception;
import com.taskmanager.system.dto.response.CustomApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex){
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//              .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        CustomApiResponse<String> response = CustomApiResponse.<String>builder()
                .success(false)
                .message(errors)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomApiResponse<String>> handleNotFound(ResourceNotFoundException ex){
        CustomApiResponse<String> response = CustomApiResponse.<String>builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Handle Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomApiResponse<String>> handleBadRequest(BadRequestException ex){
        CustomApiResponse<String> response = CustomApiResponse.<String>builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handler generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse<String>> handleGenericException(Exception ex){
        CustomApiResponse<String> response = CustomApiResponse.<String>builder()
                .success(false)
                .message("Something went wrong")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex){
        CustomApiResponse<String> response = CustomApiResponse.<String>builder()
                .success(false)
                .message("Access Denied: You do not have the required permissions(ADMIN role) to perform this action.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
