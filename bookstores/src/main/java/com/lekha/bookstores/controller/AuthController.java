package com.lekha.bookstores.controller;

import com.lekha.bookstores.dto.LoginDto;
import com.lekha.bookstores.payload.ApiResponse;
import com.lekha.bookstores.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginDto loginDto){

        String token = authService.login(loginDto);

        ApiResponse<String> response = new ApiResponse<>(
                "success",
                "Login successfully",
                token
        );

        return ResponseEntity.ok(response);
    }
}
