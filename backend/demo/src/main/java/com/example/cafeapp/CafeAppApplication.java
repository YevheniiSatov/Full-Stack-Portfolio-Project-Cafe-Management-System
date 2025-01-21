package com.example.cafeapp;

import com.example.cafeapp.entities.Cafe;
import com.example.cafeapp.entities.User;
import com.example.cafeapp.repositories.CafeRepository;
import com.example.cafeapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@SpringBootApplication
public class CafeAppApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CafeRepository cafeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(CafeAppApplication.class, args);
	}

	/**
	 * Generates a random password of the specified length.
	 * @param length The length of the password to generate.
	 * @return A randomly generated password.
	 */
	private String generateRandomPassword(int length) {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[length];
		random.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
	}

	@Override
	@Transactional
	public void run(String... args) {
		// Update or create cafe users and associated cafes
		updateCafeUserPassword("MAJ", "maj@cafe.com");
		updateCafeUserPassword("Kazacok", "kazacok@cafe.com");
		updateOrCreateCafeUser("MAJ", "maj@cafe.com", 111L);
		updateOrCreateCafeUser("Kazacok", "kazacok@cafe.com", 112L);
	}

	/**
	 * Updates or creates a cafe user and its associated cafe.
	 * @param name The name of the cafe and user.
	 * @param email The email of the cafe and user.
	 * @param cafeId The ID of the cafe.
	 */
	private void updateOrCreateCafeUser(String name, String email, Long cafeId) {
		User user = userRepository.findByEmail(email).orElse(null);
		Cafe cafe = cafeRepository.findByEmail(email).orElse(null);

		if (user == null) {
			String password = generateRandomPassword(12);
			user = new User();
			user.setName(name);
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode(password));
			user.setRole("CAFE");
			userRepository.save(user);
			System.out.println("User created: " + name + ". Login: " + email + ". Password: " + password);
		}

		if (cafe == null) {
			cafe = new Cafe();
			cafe.setId(cafeId);
			cafe.setName(name);
			cafe.setEmail(email);
			cafe.setUser(user);
			cafeRepository.save(cafe);
			System.out.println("Cafe created: " + name + " with ID: " + cafeId);
		} else {
			System.out.println("Cafe " + name + " already exists.");
		}
	}

	/**
	 * Updates the password for an existing cafe user.
	 * @param name The name of the cafe.
	 * @param email The email of the cafe user.
	 */
	private void updateCafeUserPassword(String name, String email) {
		Optional<User> existingUser = userRepository.findByEmail(email);

		if (existingUser.isPresent()) {
			User user = existingUser.get();
			String newPassword = generateRandomPassword(12);
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			System.out.println("Password updated for cafe " + name + ". Login: " + email + ". New password: " + newPassword);
		} else {
			System.out.println("Cafe " + name + " not found.");
		}
	}
}
