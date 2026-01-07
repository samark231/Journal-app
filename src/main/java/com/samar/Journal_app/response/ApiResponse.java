package com.samar.Journal_app.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(Boolean success, String message, T data){
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    public ApiResponse(Boolean success, String message){
        this.success = success;
        this.message = message;
        this.data  = null;
        this.timestamp = LocalDateTime.now();
    }

}
