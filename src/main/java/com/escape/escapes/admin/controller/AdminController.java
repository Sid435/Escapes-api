package com.escape.escapes.admin.controller;

import com.escape.escapes.admin.model.Admin;
import com.escape.escapes.admin.model.AuthRequest;
import com.escape.escapes.admin.repo.AdminRepo;
import com.escape.escapes.admin.service.AdminService;
import com.escape.escapes.token.model.JwtToken;
import com.escape.escapes.token.model.RefreshToken;
import com.escape.escapes.token.repo.RefreshTokenRepo;
import com.escape.escapes.token.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/escapes")
public class AdminController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private AdminService service;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @PostMapping("/add-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Admin> addUser(Admin admin){
        return ResponseEntity.ok(service.addUser(admin));
    }
    @GetMapping("/authenticate")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<JwtToken> authenticationTokenGeneration(AuthRequest request){
        String email = request.getEmail();

        JwtToken token = new JwtToken();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));

        if(authentication.isAuthenticated()){
            Optional<RefreshToken> refreshTokenCheck = refreshTokenRepo.findByUserEmail(email);
            RefreshToken refreshToken = new RefreshToken();
            if(refreshTokenCheck.isPresent()){
                try{
                    refreshTokenService.isTokenExpired(refreshTokenCheck.get().getToken());
                    refreshToken = refreshTokenCheck.get();
                }catch (RuntimeException e){
                    refreshToken = refreshTokenService.createRefreshToken(email);
                }
            }
            Optional<Admin> adminOption = adminRepo.findByEmail(email);
            if(adminOption.isPresent()){
                Admin admin = adminOption.get();
                token.setId(admin.getId());
                token.setEmail(admin.getEmail());
                token.setLogCount(admin.getLogCount());
                token.setRefreshToken(refreshToken.getToken());
                token.setAccessTokenExpirationDate(System.currentTimeMillis() + 24*60*60*1000);
                token.setVerification(admin.isVerification());
                token.setRefreshTokenExpirationDate(refreshToken.getExpiryDate());

                return ResponseEntity.ok(token);
            }else{
                return ResponseEntity.notFound().build();
            }

        }else{
            Optional<Admin> admin = adminRepo.findByEmail(email);
            if(admin.isPresent()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }else{
                return ResponseEntity.notFound().build();
            }
        }
    }
}
