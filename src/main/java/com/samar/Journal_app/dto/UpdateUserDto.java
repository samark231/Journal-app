package com.samar.Journal_app.dto;

import lombok.Data;

@Data
public class UpdateUserDto {
    private String email;
    private Boolean sentimentAnalysis;

}
