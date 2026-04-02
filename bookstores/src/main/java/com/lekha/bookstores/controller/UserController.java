package com.lekha.bookstores.controller;

import com.lekha.bookstores.dto.UserDto;
import com.lekha.bookstores.entity.User;
import com.lekha.bookstores.payload.ApiResponse;
import com.lekha.bookstores.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")

public class UserController {
    @Autowired
    private UserService userservice;
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto >> createUser(@Valid  @RequestBody UserDto userDto){
        UserDto user=userservice.saveUser(userDto);
        ApiResponse<UserDto> response=new ApiResponse<>("success","save user successfully",user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getUsers(){
        List<UserDto> user= userservice.getAllUsers();
        ApiResponse<List<UserDto>> response = new ApiResponse<>("sucess","get all users",user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getById(@PathVariable Long id){
        UserDto user = userservice.getUserById(id);
        ApiResponse<UserDto> response=new ApiResponse<>("success","user fetched successfully",user);
        return ResponseEntity.ok(response);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id){
        userservice.deleteById(id);
        ApiResponse<String> response = new ApiResponse<>("success","user deleted successfully",null);
        return  ResponseEntity.ok(response);
    }

}
