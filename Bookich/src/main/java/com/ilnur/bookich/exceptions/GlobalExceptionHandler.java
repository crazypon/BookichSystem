package com.ilnur.bookich.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/*
So how this works. In our AuthController we have registerUser. There we have register(), and it throws and exception
if user with given username already exists. Controller returns ReponseEntity.ok(), but if register throws an error
our handler will catch it and will return ResponseEntity(CONFLICT)

<=====================!!!IMPORTANT!!!=====================>
Why we didn't use if else checking it right there and send the proper ResponseEntity?
Well, due to the race conditions inside of Spring's internals we should better use handler.
Here what Gemini says about it:

1. Race Conditions: If two people register with "ilnur" at the exact same millisecond,
   the if-check might pass for both, causing a database crash later. Transactional Services handle this better.

2. Bloat: Your controller gets fat with logic.

3. Consistency: With the Exception Handler, every duplicate error (Users, Books, etc.)
   returns the same standardized 409 response.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    // this method handles all kinds of validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(
                error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                }
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserExists(UserAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

}
