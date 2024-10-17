package com.epsi.workshop.fig.service;

import com.epsi.workshop.fig.config.PasswordEncoder;
import com.epsi.workshop.fig.entity.UserEntity;
import com.epsi.workshop.fig.entity.UserLoginEntity;
import com.epsi.workshop.fig.repository.UserLoginRepository;
import com.epsi.workshop.fig.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity register(String username, String email, String password) throws Exception {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new Exception("Email already in use");
        }

        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setEmail(email);


        UserLoginEntity userLogin = new UserLoginEntity();
        userLogin.setUser(user);
        userLogin.setPassword(passwordEncoder.encode(password));

        userLoginRepository.save(userLogin);

        return userRepository.save(user);
    }

    public boolean login(String username, String password) throws Exception {
        UserEntity user = new UserEntity();
        if (username.contains("@")) {
            user = userRepository.findByEmail(username).orElseThrow(() -> new Exception("User not found"));
        } else {
            user = userRepository.findByUsername(username).orElseThrow(() -> new Exception("User not found"));
        }

        UserLoginEntity userLogin = userLoginRepository.findByUser(user)
                .orElseThrow(() -> new Exception("Invalid credentials"));

        return passwordEncoder.matches(password, userLogin.getPassword());
    }
}