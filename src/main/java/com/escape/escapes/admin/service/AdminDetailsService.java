package com.escape.escapes.admin.service;

import com.escape.escapes.admin.model.Admin;
import com.escape.escapes.admin.model.AdminDetails;
import com.escape.escapes.admin.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepo repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin = repo.findByEmail(username);
        return admin.map(AdminDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User Doesn't exists"));
    }
}
