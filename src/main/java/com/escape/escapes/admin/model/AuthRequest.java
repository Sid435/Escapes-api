package com.escape.escapes.admin.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"Email/Username", "Password"})
public class AuthRequest {

    @JsonProperty("Email/Username")
    private String email;

    @JsonProperty("Password")
    private String password;
}