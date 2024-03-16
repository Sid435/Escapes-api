package com.escape.escapes.token.service;

import com.escape.escapes.admin.model.Admin;
import com.escape.escapes.admin.model.AdminDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {

    public static final String SECRET = "e4f1779c78d82c72c8129c479e629d05f0df2315ffa7efac46431b7a801dbc5b";


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
