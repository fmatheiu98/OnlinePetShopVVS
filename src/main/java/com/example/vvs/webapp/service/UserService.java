package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.User;
import com.example.vvs.webapp.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{

    User save(UserRegistrationDto registrationDto);
}
