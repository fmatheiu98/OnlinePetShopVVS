package com.example.vvs.webapp.controller;

import com.example.vvs.webapp.model.User;
import com.example.vvs.webapp.service.UserService;
import com.example.vvs.webapp.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserRegistrationControllerTest {

    @Mock
    private UserRegistrationDto userRegistrationDto;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRegistrationController userRegistrationController;

    @Autowired
    MockMvc mockMvc;

    @Test
    void userRegistrationDtoWorksAsExpected() {
        assertThat(userRegistrationController.userRegistrationDto()).isInstanceOf(UserRegistrationDto.class);
    }

    @Test
    void showRegistrationFormAsExpected() {
        assertThat(userRegistrationController.showRegistrationForm()).isEqualTo("registration");
    }

    @Test
    void saveUserThenReturnTheCorrectLink() {
        User user = new User();
        when(userService.save(any(UserRegistrationDto.class))).thenReturn(user);

        String result = userRegistrationController.registerUserAccount(userRegistrationDto);

        verify(userService).save(any(UserRegistrationDto.class));
        assertThat(result).isEqualTo("redirect:/registration?success");
    }

    @Test
    void saveNewOrderWithNullDTO() {
        assertThrows(IllegalArgumentException.class,()-> userRegistrationController.registerUserAccount(null));
    }

    @Test
    @DisplayName("Get to Registration Page -> Page responds with 200 and contains expected text")
    public void whenGetToCheckoutPage_then200WithContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/registration")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("registration"))
                .andExpect(content().string(containsString("Registration Page")));
    }

    @Test
    public void whenGetToRegistration_RegisterUserThenRedirect() throws Exception {
        byte[] array = new byte[7];
        new SecureRandom().nextBytes(array);
        String email = new String(array, StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                .param("first_name","George")
                .param("last_name","Daniel")
                .param("email",email)
                .param("passwd","1234")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration?success"))
                .andExpect(view().name("redirect:/registration?success"));
    }

}