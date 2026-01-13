package com.samar.Journal_app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class JournalEntryResponse {
    private ObjectId id;
    private String title;
    private String content;
    private LocalDateTime date;

}
