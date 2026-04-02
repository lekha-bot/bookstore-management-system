package com.lekha.bookstores.service.impl;

import com.lekha.bookstores.dto.LoginDto;
import com.lekha.bookstores.entity.User;
import com.lekha.bookstores.exception.ResourceNotFoundException;
import com.lekha.bookstores.repository.UserRepository;
import com.lekha.bookstores.security.JwtUtil;
import com.lekha.bookstores.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String login(LoginDto loginDto){

        User user = userRepo.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 🔐 Check password
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        // 👉 TEMP return message (we add JWT next)
        return jwtUtil.generateToken(user.getEmail());
    }
}
