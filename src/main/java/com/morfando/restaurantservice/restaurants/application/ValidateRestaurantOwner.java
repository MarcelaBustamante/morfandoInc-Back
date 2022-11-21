package com.morfando.restaurantservice.restaurants.application;

import com.morfando.restaurantservice.restaurants.model.RestaurantRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import com.morfando.restaurantservice.users.application.FindUser;
import com.morfando.restaurantservice.users.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class ValidateRestaurantOwner {
	private final RestaurantRepository repo;
	private final FindUser findUser;

	public ValidateRestaurantOwner(RestaurantRepository repo, FindUser findUser) {
		this.repo = repo;
		this.findUser = findUser;
	}

	/**
	 * Validates ownership of restaurant by given partner
	 * @param restaurantId
	 * @param partnerEmail
	 * @return restaurant entity
	 */
	public Restaurant validateOwnership(long restaurantId, String partnerEmail) {
		User user = findUser.find(partnerEmail);
		Restaurant restaurant = repo.findById(restaurantId).orElseThrow();
		if (restaurant.getOwner() != user.getId()) {
			throw new IllegalStateException("Invalid owner");
		}
		return restaurant;
	}
}
