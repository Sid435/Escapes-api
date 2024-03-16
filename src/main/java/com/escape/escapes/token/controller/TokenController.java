package com.escape.escapes.token.controller;

import com.escape.escapes.admin.model.Admin;
import com.escape.escapes.token.model.JwtToken;
import com.escape.escapes.token.model.RefreshToken;
import com.escape.escapes.token.model.RefreshTokenRequest;
import com.escape.escapes.token.service.JwtService;
import com.escape.escapes.token.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TokenController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;


    @GetMapping("/refresh")
    public ResponseEntity<JwtToken> accessTokenRegeneration(@RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshToken refreshToken = refreshTokenService.findByTokens(refreshTokenRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));


        refreshTokenService.isTokenExpired(refreshToken.getToken());
        Admin info = refreshToken.getAdminInfo();
        String accessToken = jwtService.generateToken(info.getEmail());

        JwtToken token = new JwtToken();
        token.setId(info.getId());
        token.setEmail(info.getEmail());
        token.setToken(accessToken);
        token.setRefreshToken(refreshTokenRequest.getToken());
        token.setAccessTokenExpirationDate(System.currentTimeMillis()+1000*60*60*24);
        token.setLogCount(info.getLogCount());

        return ResponseEntity.ok(token);
    }
}
