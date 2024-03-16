package com.escape.escapes.token.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {
    private String id;
    private String email, token, refreshToken;
    private Long refreshTokenExpirationDate, accessTokenExpirationDate, logCount;
    private boolean verification;
}