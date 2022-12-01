package com.morfando.restaurantservice.users.application;

import com.morfando.restaurantservice.users.model.UserRepository;
import com.morfando.restaurantservice.users.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UpdateUser {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public User update(String email, String password, String name, String lastName, String profilePicture) {
        User user = repo.findByEmailIgnoreCase(email).orElseThrow();
        if (null != password) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if (null != name) {
            user.setName(name);
        }
        if (null != lastName) {
            user.setLastName(lastName);
        }
        if (null != profilePicture) {
            user.setProfilePicture(profilePicture);
        }
        return repo.save(user);
    }
}
