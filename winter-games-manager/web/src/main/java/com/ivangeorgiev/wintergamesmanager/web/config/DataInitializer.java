package com.ivangeorgiev.wintergamesmanager.web.config;

import com.ivangeorgiev.wintergamesmanager.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        try {
            userService.loadUserByUsername("admin");
        } catch (Exception e) {
            userService.registerAdmin("admin", "admin123");
        }
    }
}
