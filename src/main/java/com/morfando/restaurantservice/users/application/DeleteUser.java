package com.morfando.restaurantservice.users.application;

import com.morfando.restaurantservice.users.model.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteUser {
	private final UserRepository repo;

	public DeleteUser(UserRepository repo) {
		this.repo = repo;
	}

	public void delete(String email) {
		repo.deleteByEmailIgnoreCase(email);
	}
}
