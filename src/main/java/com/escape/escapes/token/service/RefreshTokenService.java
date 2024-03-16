package com.escape.escapes.token.service;

import com.escape.escapes.admin.repo.AdminRepo;
import com.escape.escapes.token.model.RefreshToken;
import com.escape.escapes.token.repo.RefreshTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepo refreshTokenRepository;
    @Autowired
    private AdminRepo userInfoRepository;

    private final String SECRET = "b5d4594666a53ebd74d5b4a3157b8d00f8238c1343861e34dd0f73f2befdbb04";

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .adminInfo(userInfoRepository.findByEmail(username).get())
                .token(generateToken(username))
                .expiryDate(Calendar.getInstance().getTimeInMillis() + 1000L *60*60*24*30*3)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public String extractUsername(String token){ return extractClaim(token, Claims::getSubject);}

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        return claimsResolver.apply(Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenExpired(String token){
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
                .expiration(new Date(System.currentTimeMillis() + 24L *60*60*1000*30))
                .signWith(getSignKey()).compact();
    }

    private SecretKeySpec getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public Optional<RefreshToken> findByTokens(String token){
        return refreshTokenRepository.findByToken(token);
    }
}
