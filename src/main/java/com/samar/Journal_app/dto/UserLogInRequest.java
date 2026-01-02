package com.samar.Journal_app.dto;

import lombok.Data;

@Data
public class UserLogInRequest {
    private String usernameOrEmail;
    private String password;
}   
