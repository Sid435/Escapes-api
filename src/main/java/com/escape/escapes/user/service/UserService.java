package com.escape.escapes.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.escape.escapes.user.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    
    
}
