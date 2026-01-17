package com.samar.Journal_app.dto;

import com.samar.Journal_app.enums.Gender;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private Gender gender;
    private Boolean sentimentAnalysis;

}
