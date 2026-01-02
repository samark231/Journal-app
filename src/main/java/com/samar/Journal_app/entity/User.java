package com.samar.Journal_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    @NotBlank(message = "Username can not be empty")
    @Size(min=6, message = "Username must be atleast 6 characters")
    private String username;
    private String firstName;
    private String lastName;

    @Email(message = "Invalid Email")
    private String email;
    @Builder.Default
    private Boolean sentimentAnalysis = false;

    @JsonIgnore
    @NonNull
    private String password;

    @JsonIgnore
    @Builder.Default
    @DBRef//works as a foreign key to other collection.
    private List<JournalEntry> journalEntries  = new ArrayList<>();
    private List<String> roles;
}
