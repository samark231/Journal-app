package com.samar.Journal_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CreateJournalEntryRequest {
    @NotBlank
    private String title;
    private String content;
    private LocalDateTime date;
}
