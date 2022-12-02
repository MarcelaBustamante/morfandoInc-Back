package com.morfando.restaurantservice.users.api;

import com.morfando.restaurantservice.users.api.dto.AuthenticationResult;
import com.morfando.restaurantservice.users.api.dto.ModifiedUser;
import com.morfando.restaurantservice.users.api.dto.UserCredentials;
import com.morfando.restaurantservice.users.api.dto.NewUser;
import com.morfando.restaurantservice.users.application.*;
import com.morfando.restaurantservice.users.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.morfando.restaurantservice.restaurants.application.GetRestaurants;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {

	//	@PreAuthorize("hasAuthority('SCOPE_CLIENT')")
	//	@PreAuthorize("hasAuthority('SCOPE_PARTNER')")

	private final FindUser findUser;
	private final Authenticate authenticate;
	private final CreateUser createUser;
	private final DeleteUser deleteUser;
	private final GetRestaurants getRestaurants;
	private final HandleFavourite handleFavourite;
	private final UpdateUser updateUser;

	@PostMapping("/login")
	public AuthenticationResult login(@RequestBody @Valid UserCredentials credentials) {
		User user = findUser.find(credentials.getEmail());
		return new AuthenticationResult(authenticate.authenticate(user, credentials.getPassword()), false);
	}

	@PostMapping("/oauth")
	public AuthenticationResult googleAuthentication(@RequestBody String googleTokenId) {
		Pair<Jwt, Boolean> result = authenticate.googleAuthentication(googleTokenId);
		return new AuthenticationResult(result.getLeft(), result.getRight());
	}

	@GetMapping("/{user-id}")
	public User findUserById(@PathVariable("user-id") long id, Authentication authentication) {
		return findUser.findById(id, authentication.getName());
	}

	@PostMapping
	public User createUser(@RequestBody @Valid NewUser newUser) {
		return createUser.createPartner(newUser.getEmail(), newUser.getPassword(), newUser.getName(), newUser.getLastName(),
				newUser.getProfilePicture());
	}

	@PutMapping
	public User updateUser(@RequestBody @Valid ModifiedUser modifiedUser, Authentication authentication) {
		String email = authentication.getName();
		return updateUser.update(email, modifiedUser.getPassword(), modifiedUser.getName(), modifiedUser.getLastName(),
				modifiedUser.getProfilePicture());
	}

	@DeleteMapping
	@Transactional
	public void deleteMyUser(Authentication authentication) {
		deleteUser.delete(authentication.getName());
	}

	@PostMapping("/addfavourite/{user-id}/{restaurant-id}")
	public void addFavourite(@PathVariable("user-id") long idUser,@PathVariable("restaurant-id") long idRestaurant) {
		handleFavourite.addFavourite(idUser, getRestaurants.getById(idRestaurant));
	}

	@PostMapping("/deletefavourite/{user-id}/{restaurant-id}")
	public void deleteFavourite(@PathVariable("user-id") long idUser,@PathVariable("restaurant-id") long idRestaurant) {
		handleFavourite.deleteFavourite(idUser, getRestaurants.getById(idRestaurant));
	}

	@GetMapping("/getfavourite/{user-id}/{restaurant-id}")
	public void getFavourite(@PathVariable("user-id") long idUser,@PathVariable("restaurant-id") long idRestaurant) {
		handleFavourite.getFavourite(idUser, getRestaurants.getById(idRestaurant));
	}


}