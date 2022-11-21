package com.morfando.restaurantservice.users.model;

import com.morfando.restaurantservice.users.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmailIgnoreCase(String email);

	void deleteByEmailIgnoreCase(String email);
}
