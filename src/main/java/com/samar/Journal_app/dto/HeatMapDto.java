package com.samar.Journal_app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
public class HeatMapDto {
    @Id
    private String date;
    private Long count;
}
