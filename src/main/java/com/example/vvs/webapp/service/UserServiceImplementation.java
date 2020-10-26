package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.User;
import com.example.vvs.webapp.repository.UserRepo;
import com.example.vvs.webapp.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class UserServiceImplementation implements UserService{

    private UserRepo userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImplementation(UserRepo userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getFirst_name(),
                registrationDto.getLast_name(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPasswd()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        ArrayList<SimpleGrantedAuthority> arr = new ArrayList<>();
        arr.add(new SimpleGrantedAuthority("USER"));
        if(user == null)
            throw new IllegalArgumentException("Invalid username or password!");
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswd(),arr);
    }
}
