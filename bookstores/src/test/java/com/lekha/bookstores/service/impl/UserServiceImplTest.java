package com.lekha.bookstores.service.impl;



import com.lekha.bookstores.dto.UserDto;
import com.lekha.bookstores.entity.User;
import com.lekha.bookstores.exception.ResourceNotFoundException;
import com.lekha.bookstores.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userrepo;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testSaveUser_success() {
        // Arrange
        UserDto inputDto = new UserDto();
        inputDto.setName("Lekha");
        inputDto.setEmail("lekha@gmail.com");
        inputDto.setPassword("plainPassword");
        inputDto.setRole("USER");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Lekha");
        savedUser.setEmail("lekha@gmail.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole("USER");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userrepo.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDto result = userService.saveUser(inputDto);

        // Assert
        assertEquals("Lekha", result.getName());
        assertEquals("lekha@gmail.com", result.getEmail());
        verify(userrepo, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllUsers_returnsList() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Lekha");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Ramesh");

        when(userrepo.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Lekha", result.get(0).getName());
    }

    @Test
    void testGetUserById_found() {
        User user = new User();
        user.setId(1L);
        user.setName("Lekha");

        when(userrepo.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertEquals("Lekha", result.getName());
    }

    @Test
    void testGetUserById_notFound_throwsException() {
        when(userrepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(99L);
        });
    }

    @Test
    void testDeleteById_success() {
        User user = new User();
        user.setId(1L);

        when(userrepo.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteById(1L);

        verify(userrepo, times(1)).delete(user);
    }

    @Test
    void testDeleteById_notFound_throwsException() {
        when(userrepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteById(99L);
        });
    }
}
