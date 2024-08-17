package com.escape.escapes.admin.service;

import com.escape.escapes.admin.model.Admin;
import com.escape.escapes.admin.model.AuthRequest;
import com.escape.escapes.admin.repo.AdminRepo;
import com.escape.escapes.otp.OtpModel;
import com.escape.escapes.otp.OtpRepo;
import com.escape.escapes.token.model.JwtToken;
import com.escape.escapes.token.model.RefreshToken;
import com.escape.escapes.token.repo.RefreshTokenRepo;
import com.escape.escapes.token.service.JwtService;
import com.escape.escapes.token.service.RefreshTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private OtpRepo otpRepo;

    public Admin addUser(Admin admin){
        admin.setId(UUID.randomUUID().toString().split("-")[0]);
        admin.setPassword(encoder.encode(admin.getPassword()));
        admin.setVerification(false);

        return adminRepo.save(admin);
    }

    public ResponseEntity<OtpModel> sendOtpMethod(String email){
        Optional<Admin> userPresent = adminRepo.findByEmail(email);
        if(userPresent.isPresent()){
            OtpModel otpModel = new OtpModel();
            otpModel.setOtpCode(String.valueOf(randomNumber()));
            otpModel.setUser(userPresent.get());
            otpModel.setExpirationTime(System.currentTimeMillis() + 5*60*60*1000);
            otpRepo.save(otpModel);
            sendMessage(email, otpModel.getOtpCode(), "Your OTP is ");

            return ResponseEntity.ok(otpModel);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<JwtToken> verifyOtp(AuthRequest request){
        Optional<OtpModel> otpPresent = otpRepo.findByEmail(request.getEmail());
        if(otpPresent.isPresent()){
            if(otpPresent.get().equals(request.getPassword())){ // Otp is passed as OTP
                AuthRequest authRequest = new AuthRequest();
                authRequest.setEmail(request.getEmail());
                return authAndgenerateTokenByOtp(authRequest);
            }
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<JwtToken> authAndgenerateTokenByOtp(AuthRequest request){
        String email = request.getEmail();
        JwtToken token = new JwtToken();

        Optional<RefreshToken> refreshTokenCheck = refreshTokenRepo.findByUserEmail(email);
        RefreshToken refreshToken = new RefreshToken();
        if(refreshTokenCheck.isPresent()){
            try{
                refreshTokenService.isTokenExpired(refreshTokenCheck.get().getToken());
                refreshToken = refreshTokenCheck.get();
            }catch (RuntimeException e){
                refreshToken = refreshTokenService.createRefreshToken(email);
            }
        }else{
            refreshToken = refreshTokenService.createRefreshToken(email);
        }

        Optional<Admin> adminOption = adminRepo.findByEmail(email);
        if(adminOption.isPresent()){
            Admin admin = adminOption.get();
            token.setId(admin.getId());
            token.setEmail(admin.getEmail());
            token.setLogCount(admin.getLogCount());
            token.setToken(jwtService.generateToken(email));
            token.setRefreshToken(refreshToken.getToken());
            token.setAccessTokenExpirationDate(System.currentTimeMillis() + 24*60*60*1000);
            token.setVerification(admin.isVerification());
            token.setRefreshTokenExpirationDate(refreshToken.getExpiryDate());

            return ResponseEntity.ok(token);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<JwtToken> authAndgenerateToken(AuthRequest request){
        String email = request.getEmail();
        JwtToken token = new JwtToken();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword().toString()));
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
            }else{
                refreshToken = refreshTokenService.createRefreshToken(email);
            }

            Optional<Admin> adminOption = adminRepo.findByEmail(email);
            if(adminOption.isPresent()){
                Admin admin = adminOption.get();
                token.setId(admin.getId());
                token.setEmail(admin.getEmail());
                token.setLogCount(admin.getLogCount());
                token.setToken(jwtService.generateToken(email));
                token.setRefreshToken(refreshToken.getToken());
                token.setAccessTokenExpirationDate(System.currentTimeMillis() + 24*60*60*1000);
                token.setVerification(admin.isVerification());
                token.setRefreshTokenExpirationDate(refreshToken.getExpiryDate());

                return ResponseEntity.ok(token);
            }else{
                return ResponseEntity.notFound().build();
            }

        }else{

            System.out.println("Failed");
            Optional<Admin> admin = adminRepo.findByEmail(email);
            if(admin.isPresent()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }else{
                return ResponseEntity.notFound().build();
            }
        }
    }

    public int randomNumber(){
        int min = 100000;
        int max = 999999;
        Random random = new Random();

        return random.nextInt(max - min + 1) + min;
    }


    public void sendMessage(String email, String text, String body){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("aryansood12@gmail.com");
        message.setSubject("OTP VERIFICATION");
        message.setText(body + "\n Otp is : "+ text);

        mailSender.send(message);
    }

}
