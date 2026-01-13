package com.samar.Journal_app.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class JournalEntryResponse {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String title;
    private String content;
    private LocalDateTime date;

}
