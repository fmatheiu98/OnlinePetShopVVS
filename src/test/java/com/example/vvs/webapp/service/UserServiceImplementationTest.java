package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.User;
import com.example.vvs.webapp.repository.UserRepo;
import com.example.vvs.webapp.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplementationTest {

    private UserRegistrationDto mydto = new UserRegistrationDto();

    @Mock
    private UserRepo myrepo;

    private BCryptPasswordEncoder pw;

    private UserServiceImplementation userServiceImplementation;

    @BeforeEach
    void setUp()
    {
        mydto = new UserRegistrationDto();
        pw = new BCryptPasswordEncoder();
        userServiceImplementation = new UserServiceImplementation(myrepo,pw);
    }

    @Test
    void saveUserInDatabase_OK() {

        mydto.setFirst_name("Flavius");
        mydto.setLast_name("Test123");
        mydto.setEmail("test@yahoo.commmm");
        mydto.setPasswd("1234");

        User userExisting = new User(mydto.getFirst_name(),
                mydto.getLast_name(),
                mydto.getEmail(),
                pw.encode(mydto.getPasswd()));

        mydto.setFirst_name("Flavius");
        mydto.setLast_name("Test123");
        mydto.setEmail("test@yahoo.com");
        mydto.setPasswd("1234");

        User user = new User(mydto.getFirst_name(),
                mydto.getLast_name(),
                mydto.getEmail(),
                pw.encode(mydto.getPasswd()));

        when(myrepo.findByEmail(anyString())).thenReturn(null);

        when(myrepo.save(any(User.class))).thenReturn(user);

        User savedUser = userServiceImplementation.save(mydto);
        assertEquals(user,savedUser);
    }

    @Test
    void saveUserInDatabase_Error_Same_Email() {

        mydto.setFirst_name("Flavius");
        mydto.setLast_name("Test123");
        mydto.setEmail("test@yahoo.com");
        mydto.setPasswd("1234");

        User userExisting = new User(mydto.getFirst_name(),
                mydto.getLast_name(),
                mydto.getEmail(),
                pw.encode(mydto.getPasswd()));


        mydto.setFirst_name("Flavius545");
        mydto.setLast_name("Test123543");
        mydto.setEmail("test@yahoo.com");
        mydto.setPasswd("12346456");

        User user = new User(mydto.getFirst_name(),
                mydto.getLast_name(),
                mydto.getEmail(),
                pw.encode(mydto.getPasswd()));

        when(myrepo.findByEmail(anyString())).thenReturn(userExisting);

//        when(myrepo.save(any(User.class))).thenReturn(user);

        User savedUser = userServiceImplementation.save(mydto);
        assertEquals(null,savedUser);
    }

    @Test
    void saveUserInDatabase_WithNullUser()
    {
        assertThrows(IllegalArgumentException.class, () -> userServiceImplementation.save(mydto));
    }

    @Test
    void loadUserByUsername_WithNoMocksTest()
    {
        mydto.setFirst_name("Flavius");
        mydto.setLast_name("Test123");
        mydto.setEmail("test@yahoo.com");
        mydto.setPasswd("1234");

        userServiceImplementation.save(mydto);
        User user = new User(mydto.getFirst_name(),
                mydto.getLast_name(),
                mydto.getEmail(),
                pw.encode(mydto.getPasswd()));

        when(myrepo.findByEmail("test@yahoo.com")).thenReturn(user);

        UserDetails foundUser = userServiceImplementation.loadUserByUsername("test@yahoo.com");

        verify(myrepo,times(2)).findByEmail(anyString());

        assertThat(foundUser.getUsername()).isEqualTo("test@yahoo.com");
    }
}
