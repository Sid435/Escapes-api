package com.escape.escapes.user.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "Users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String profile_pic;
    private String occupation;
    private long date_of_birth;
    private String bio;
}
