package com.escape.escapes.admin.controller;

import com.escape.escapes.admin.model.Admin;
import com.escape.escapes.admin.model.AuthRequest;
import com.escape.escapes.admin.service.AdminService;
import com.escape.escapes.otp.OtpModel;
import com.escape.escapes.token.model.JwtToken;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/escapes")
public class AdminController {


    @Autowired
    private AdminService service;

    @PostMapping("/add-user")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Admin> addUser(@RequestBody Admin admin){
        return ResponseEntity.ok(service.addUser(admin));
    }
    /*
     * Login with email and otp
     * Login with email and password
     */

    @PostMapping("/send-otp")
    public ResponseEntity<OtpModel> sendOtp(@RequestParam String email) {
        return service.sendOtpMethod(email);
    }

    @PostMapping("/verify")
    public ResponseEntity<JwtToken> verifyOtp(@RequestBody AuthRequest request){
        return service.verifyOtp(request);
    }

    @GetMapping("/login-by-username-password")
    public ResponseEntity<JwtToken> authByUandP(@RequestBody AuthRequest request) {
        return service.authAndgenerateToken(request);
    }
}
