package com.morfando.restaurantservice.users.application;

import com.morfando.restaurantservice.users.model.UserRepository;
import com.morfando.restaurantservice.users.model.entity.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class FindUser {
	private final UserRepository repo;

	public FindUser(UserRepository repo) {
		this.repo = repo;
	}

	public User find(String email) {
		return repo.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
	}

	public User findById(long id, String name) {
		User user = repo.findById(id).orElseThrow();
		if (name.equals(user.getEmail())) {
			return user;
		}
		return User.publicUser(user);
	}
}
