package com.example.vvs.webapp.controller;

import com.example.vvs.webapp.model.User;
import com.example.vvs.webapp.service.UserService;
import com.example.vvs.webapp.web.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto()
    {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm()
    {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto)
    {
        if(registrationDto==null)
        {
            throw new IllegalArgumentException();
        }
        User usr = userService.save(registrationDto);
        if(usr!=null)
            return "redirect:/registration?success";
        return "redirect:/registration?failure";
    }
}
