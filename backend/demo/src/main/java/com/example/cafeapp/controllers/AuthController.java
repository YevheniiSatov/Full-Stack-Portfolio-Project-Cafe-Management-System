package com.example.cafeapp.controllers;

import com.example.cafeapp.entities.User;
import com.example.cafeapp.services.AuthService;
import com.example.cafeapp.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils; // Inject JwtUtils for JWT token operations

    /**
     * Handles user registration requests.
     * @param user User details including name, email, password, and role.
     * @return ResponseEntity containing the registered user or a bad request status if registration fails.
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        Optional<User> registeredUser = authService.registerUser(
                user.getName(), user.getEmail(), user.getPassword(), user.getRole()
        );
        return registeredUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * Handles user login requests.
     * @param loginRequest Contains email and password for authentication.
     * @return ResponseEntity with JWT token and user details if login is successful.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = authService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (user.isPresent()) {
            String token = jwtUtils.generateJwtToken(user.get()); // Generate JWT token for authenticated user
            return ResponseEntity.ok(new JwtResponse(user.get(), token, user.get().getRole()));
        } else {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
    }

    /**
     * Handles courier login requests.
     * @param loginRequest Contains email and password for authentication.
     * @return ResponseEntity with JWT token and courier details if login is successful.
     */
    @PostMapping("/login-courier")
    public ResponseEntity<JwtResponse> loginCourier(@RequestBody LoginRequest loginRequest) {
        logger.info("Courier login request received for email: {}", loginRequest.getEmail());

        Optional<User> user = authService.loginCourier(loginRequest.getEmail(), loginRequest.getPassword());

        if (user.isPresent()) {
            String token = jwtUtils.generateJwtToken(user.get());
            logger.info("Successful login for courier: {}. Token generated.", user.get().getEmail());

            if (!"COURIER".equals(user.get().getRole())) {
                logger.warn("User with email {} is not a courier. Role: {}", user.get().getEmail(), user.get().getRole());
                return ResponseEntity.status(403).body(null);  // Forbidden if user is not a courier
            }

            return ResponseEntity.ok(new JwtResponse(user.get(), token, user.get().getRole()));
        } else {
            logger.warn("Failed login attempt for email: {}", loginRequest.getEmail());
            return ResponseEntity.status(401).build(); // Unauthorized
        }
    }

    /**
     * Handles courier registration requests.
     * @param user Courier details including name, email, and password.
     * @return ResponseEntity containing the registered courier or a bad request status if registration fails.
     */
    @PostMapping("/register-courier")
    public ResponseEntity<User> registerCurier(@RequestBody User user) {
        Optional<User> registeredUser = authService.registerCurier(
                user.getName(), user.getEmail(), user.getPassword(), user.getRole()
        );
        return registeredUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
