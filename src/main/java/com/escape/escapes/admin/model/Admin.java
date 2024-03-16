package com.escape.escapes.admin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "AdminDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "email","password","roles", "loginCount", "verification"})
public class Admin {
    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("roles")
    private String roles;

    @JsonProperty("loginCount")
    private long logCount;

    @JsonProperty("verification")
    private boolean verification;
}
