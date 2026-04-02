package com.lekha.bookstores.service;

import com.lekha.bookstores.dto.UserDto;
import com.lekha.bookstores.entity.User;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto userdto);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void deleteById(Long id);

}
