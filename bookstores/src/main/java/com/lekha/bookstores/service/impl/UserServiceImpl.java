package com.lekha.bookstores.service.impl;

import com.lekha.bookstores.dto.ProductDto;
import com.lekha.bookstores.dto.UserDto;
import com.lekha.bookstores.entity.Product;
import com.lekha.bookstores.entity.User;
import com.lekha.bookstores.exception.ResourceNotFoundException;
import com.lekha.bookstores.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.lekha.bookstores.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userrepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserDto saveUser(UserDto userdto){
        User user=mapToEntity(userdto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saveUsers= userrepo.save(user);

        return mapToDto(saveUsers);


    }
    @Override
    public List<UserDto> getAllUsers(){
        List<User> user= userrepo.findAll();
      return  user.stream()
              .map(this::mapToDto)
              .toList();
    }
    @Override
    public UserDto getUserById(Long id){
        User user=userrepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("user not found"));
        return mapToDto(user);
    }
    @Override
    public void deleteById(Long id){
        User user = userrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userrepo.delete(user);
    }
    public UserDto mapToDto(User user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        //dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    public User mapToEntity(UserDto userDto){
        User user=new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return user;
    }
}
