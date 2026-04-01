package com.ecommerce;

import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User("testuser", "test@example.com", "encodedPassword"));

        User user = userService.registerUser("testuser", "test@example.com", "password");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.registerUser("testuser", "test@example.com", "password"));
    }
}