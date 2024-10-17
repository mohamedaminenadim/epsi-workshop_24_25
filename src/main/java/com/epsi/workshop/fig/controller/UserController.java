package com.epsi.workshop.fig.controller;

import com.epsi.workshop.fig.entity.UserEntity;
import com.epsi.workshop.fig.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String handleRegister(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        try {
            UserEntity user = userService.register(username, email, password);
            return "redirect:/index?success=You have been registered successfully!";
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return "redirect:/register?error=" + errorMessage;
        }
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password) {
        try {
            boolean isAuthenticated = userService.login(username, password);
            if (isAuthenticated) {
                return "redirect:/home";
            } else {
                return "redirect:/error?message=Unauthorized";
            }
        } catch (Exception e) {
            return "redirect:/index?error=invalid_credentials";
        }
    }
}
