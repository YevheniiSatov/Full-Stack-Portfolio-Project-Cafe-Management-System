package com.example.cafeapp.config;

import com.example.cafeapp.entities.User;
import com.example.cafeapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CafeUserInitialization implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Generates a random password of the specified length.
     * This method uses a secure random generator to create a Base64-encoded string.
     * @param length Length of the password to be generated.
     * @return A randomly generated password as a string.
     */
    private String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
    }

    @Override
    public void run(ApplicationArguments args) {
        // Initialize a user for the first cafe
        if (!userRepository.existsByEmail("first_cafe@cafe.com")) {
            String cafePassword = generateRandomPassword(12); // Generate a random password
            User cafeUser = new User();
            cafeUser.setName("First Cafe");
            cafeUser.setEmail("first_cafe@cafe.com");
            cafeUser.setPassword(passwordEncoder.encode(cafePassword));
            cafeUser.setRole("CAFE");
            userRepository.save(cafeUser);
            System.out.println("Cafe user created. Login: first_cafe@cafe.com Password: " + cafePassword);
        }

        // Initialize a user for the second cafe
        if (!userRepository.existsByEmail("second_cafe@cafe.com")) {
            String cafePassword = generateRandomPassword(12); // Generate a random password
            User cafeUser = new User();
            cafeUser.setName("Second Cafe");
            cafeUser.setEmail("second_cafe@cafe.com");
            cafeUser.setPassword(passwordEncoder.encode(cafePassword));
            cafeUser.setRole("CAFE");
            userRepository.save(cafeUser);
            System.out.println("Cafe user created. Login: second_cafe@cafe.com Password: " + cafePassword);
        }
    }
}
