package com.example.cafeapp.config;

import com.example.cafeapp.repositories.UserRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CleanupCafeData {

    @Autowired
    private UserRepository userRepository;

    /**
     * Method to clean up cafe user accounts before the application shuts down.
     * This ensures any sensitive or test data is removed from the database.
     */
    @PreDestroy
    public void cleanupCafeData() {
        userRepository.deleteByEmail("first_cafe@cafe.com");
        userRepository.deleteByEmail("second_cafe@cafe.com");
    }
}
