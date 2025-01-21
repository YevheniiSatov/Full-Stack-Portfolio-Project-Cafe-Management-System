package com.example.cafeapp.services;

import com.example.cafeapp.controllers.AuthController;
import com.example.cafeapp.entities.User;
import com.example.cafeapp.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Initializes cafe users on application startup.
     * This ensures that default users for cafes are created if they do not exist.
     */
    @PostConstruct
    public void initCafeUsers() {
        createCafeUser("MAJ", "maj_password");
        createCafeUser("Kazacok", "kazacok_password");
    }

    /**
     * Creates a cafe user with the provided name and password if the user does not already exist.
     * @param name The name of the cafe.
     * @param password The password for the cafe user.
     */
    private void createCafeUser(String name, String password) {
        if (!userRepository.existsByEmail(name + "@cafe.com")) {
            User user = new User();
            user.setName(name);
            user.setEmail(name + "@cafe.com"); // Mock email address
            user.setPassword(passwordEncoder.encode(password));
            user.setRole("CAFE");
            userRepository.save(user);
        }
    }

    /**
     * Registers a new user.
     * @param name The name of the user.
     * @param email The email address of the user.
     * @param password The password for the user.
     * @param role The role assigned to the user (defaults to "USER" if null).
     * @return An Optional containing the registered user, or an empty Optional if the email is already taken.
     */
    public Optional<User> registerUser(String name, String email, String password, String role) {
        if (userRepository.existsByEmail(email)) {
            return Optional.empty(); // Email is already taken
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role != null ? role : "USER");

        User savedUser = userRepository.save(user);
        return Optional.of(savedUser);
    }

    /**
     * Authenticates a user by verifying their email and password.
     * @param email The email address of the user.
     * @param password The password provided by the user.
     * @return An Optional containing the authenticated user, or an empty Optional if authentication fails.
     */
    public Optional<User> loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        } else {
            return Optional.empty(); // Invalid email or password
        }
    }

    /**
     * Authenticates a courier by verifying their email and password.
     * @param email The email address of the courier.
     * @param password The password provided by the courier.
     * @return An Optional containing the authenticated courier, or an empty Optional if authentication fails.
     */
    public Optional<User> loginCourier(String email, String password) {
        logger.info("Courier login attempt with email: {}", email);

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            logger.info("User found in database. Verifying password...");

            if (passwordEncoder.matches(password, user.get().getPassword())) {
                logger.info("Password verified for user: {}", email);
                return user;
            } else {
                logger.warn("Invalid password for user: {}", email);
            }
        } else {
            logger.warn("User with email {} not found", email);
        }

        return Optional.empty();
    }

    /**
     * Registers a new courier.
     * @param name The name of the courier.
     * @param email The email address of the courier.
     * @param password The password for the courier.
     * @param role The role assigned to the courier (defaults to "COURIER" if null).
     * @return An Optional containing the registered courier, or an empty Optional if the email is already taken.
     */
    public Optional<User> registerCurier(String name, String email, String password, String role) {
        if (userRepository.existsByEmail(email)) {
            return Optional.empty(); // Email is already taken
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role != null ? role : "COURIER");

        User savedUser = userRepository.save(user);
        return Optional.of(savedUser);
    }
}
