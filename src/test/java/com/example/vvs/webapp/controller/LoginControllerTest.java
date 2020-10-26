package com.example.vvs.webapp.controller;

import com.example.vvs.webapp.model.User;
import com.example.vvs.webapp.repository.UserRepo;
import com.example.vvs.webapp.service.UserServiceImplementation;
import com.example.vvs.webapp.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class LoginControllerTest {

    @InjectMocks
    LoginController loginController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserServiceImplementation userServiceImplementation;

    private UserRegistrationDto userRegistrationDto;

    @Test
    @DisplayName("Metoda login functioneaza corect")
    void login() {
        assertEquals("login", loginController.login());
    }

    @Test
    void loginPost()
    {
        assertEquals("redirect:/", loginController.loginPost());
    }

    @Test
    public void whenGetLoginPage_then200WithContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/login")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("login"))
                .andExpect(content().string(containsString("Login Page")));
    }

    @Test
    public void whenGetLoginPage_thenFillInLoginInfoAndRedirect() throws Exception {

        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setFirst_name("Flavius");
        userRegistrationDto.setLast_name("Test123");
        userRegistrationDto.setEmail("test@yahoo.com");
        userRegistrationDto.setPasswd("1234");

        userServiceImplementation.save(userRegistrationDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("username","test@yahoo.com")
                .param("password","1234")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void whenGetLoginPage_thenFillInBadCredentialsAndRedirectToLoginError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("username","test")
                .param("password","test1")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
}