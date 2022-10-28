package com.morfando.restaurantservice.users.api;

import com.morfando.restaurantservice.users.api.dto.AuthenticationResult;
import com.morfando.restaurantservice.users.api.dto.UserCredentials;
import com.morfando.restaurantservice.users.api.dto.NewUser;
import com.morfando.restaurantservice.users.application.Authenticate;
import com.morfando.restaurantservice.users.application.CreateUser;
import com.morfando.restaurantservice.users.application.DeleteUser;
import com.morfando.restaurantservice.users.application.FindUser;
import com.morfando.restaurantservice.users.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {

	//	@PreAuthorize("hasAuthority('SCOPE_CLIENT')")
	//	@PreAuthorize("hasAuthority('SCOPE_PARTNER')")

	private final FindUser findUser;
	private final Authenticate authenticate;
	private final CreateUser createUser;
	private final DeleteUser deleteUser;

	public UsersController(FindUser findUser, Authenticate authenticate, CreateUser createUser, DeleteUser deleteUser) {
		this.findUser = findUser;
		this.authenticate = authenticate;
		this.createUser = createUser;
		this.deleteUser = deleteUser;
	}

	@PostMapping("/login")
	public AuthenticationResult login(@RequestBody UserCredentials credentials) {
		User user = findUser.find(credentials.getEmail());
		return new AuthenticationResult(authenticate.authenticate(user, credentials.getPassword()));
	}

	@PostMapping("/oauth")
	public AuthenticationResult googleAuthentication(@RequestBody String googleTokenId) {
		return new AuthenticationResult(authenticate.googleAuthentication(googleTokenId));
	}

	@GetMapping("/{user-id}")
	public User findUserById(@PathVariable("user-id") long id, Authentication authentication) {
		return findUser.findById(id, authentication.getName());
	}

	@PostMapping
	public User createUser(@RequestBody NewUser newUser) {
		return createUser.createPartner(newUser.getEmail(), newUser.getPassword(), newUser.getName(), newUser.getLastName(),
				newUser.getProfilePicture());
	}

	@DeleteMapping
	@Transactional
	public void deleteMyUser(Authentication authentication) {
		deleteUser.delete(authentication.getName());
	}
}
