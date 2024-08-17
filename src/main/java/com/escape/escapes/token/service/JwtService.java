package com.escape.escapes.token.service;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {

    public static final String SECRET = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";


    public String extractUsername(String token){ return extractClaim(token, Claims::getSubject);}

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        return claimsResolver.apply(Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload());
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public boolean validateToken(String token, UserDetails user){
        final String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    public String generateToken(String username){
        Map<String, Objects> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Objects> claims, String  username){
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                .signWith(getSignKey()).compact();
    }

    private SecretKeySpec getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

}
