package com.escape.escapes.admin.service;

import com.escape.escapes.admin.model.Admin;
import com.escape.escapes.admin.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private PasswordEncoder encoder;

    public Admin addUser(Admin admin){
        admin.setId(UUID.randomUUID().toString().split("-")[0]);
        admin.setPassword(encoder.encode(admin.getPassword()));
        admin.setVerification(false);

        return adminRepo.save(admin);
    }

}
