package com.samar.Journal_app.exception;

import com.samar.Journal_app.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class) // <--- The "Trigger"
    public ResponseEntity<String> handleNullPointer(NullPointerException ex) {
        return new ResponseEntity<>("Ouch! Something was null.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleAllErrors(Exception e){
        ApiResponse response =  new ApiResponse<>(false, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
