package com.morfando.restaurantservice.users.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailIgnoreCase(String email);

	void deleteByEmail(String email);
}
