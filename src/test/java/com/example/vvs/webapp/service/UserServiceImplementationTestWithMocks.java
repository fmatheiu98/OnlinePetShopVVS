package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.User;
import com.example.vvs.webapp.repository.UserRepo;
import com.example.vvs.webapp.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTestWithMocks {

    @Mock
    private BCryptPasswordEncoder pw;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserRegistrationDto dto;

    @InjectMocks
    private UserServiceImplementation userServiceImplementation;

    @Test
    void saveUserInDatabase_OK() {
        User user = new User();
        user.setEmail("email123");
        when(userRepo.save(any(User.class))).thenReturn(user);

        User savedUser = userServiceImplementation.save(dto);

        verify(userRepo).save(any(User.class));
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void loadUserByUsernameWithMocks() {
        User user = new User();
        user.setPasswd("test");
        user.setEmail("test");
        when(userRepo.findByEmail(anyString())).thenReturn(user);

        UserDetails foundUser = userServiceImplementation.loadUserByUsername("test");

        verify(userRepo).findByEmail(anyString());
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(user.getEmail());
    }
}