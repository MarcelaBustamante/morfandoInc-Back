package com.morfando.restaurantservice.users.application;

import com.morfando.restaurantservice.users.model.UserRepository;
import com.morfando.restaurantservice.users.model.entity.User;
import com.morfando.restaurantservice.users.model.entity.UserType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateUser {
	private final UserRepository repo;

	private final PasswordEncoder passwordEncoder;

	public CreateUser(UserRepository repo, PasswordEncoder passwordEncoder) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
	}

	public User createPartner(String email, String password, String name, String lastName, String profilePicture) {
		Optional<User> opt = repo.findByEmailIgnoreCase(email);
		if (opt.isPresent()) {
			throw new IllegalArgumentException("Email in use");
		}
		password = passwordEncoder.encode(password);
		email = email.toLowerCase();
		User user = new User(name, lastName, email, password, profilePicture, UserType.PARTNER);
		return repo.save(user);
	}


}
