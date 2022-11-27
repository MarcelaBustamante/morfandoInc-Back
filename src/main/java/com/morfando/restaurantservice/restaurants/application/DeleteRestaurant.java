package com.morfando.restaurantservice.restaurants.application;

import com.morfando.restaurantservice.restaurants.model.RestaurantRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteRestaurant {

	private final RestaurantRepository repo;
	private final ValidateRestaurantOwner validateRestaurantOwner;

	public DeleteRestaurant(RestaurantRepository repo, ValidateRestaurantOwner validateRestaurantOwner) {
		this.repo = repo;
		this.validateRestaurantOwner = validateRestaurantOwner;
	}

	@Transactional
	public void delete(long restaurantId, String partnerEmail) {
		Restaurant restaurant = validateRestaurantOwner.validateOwnership(restaurantId, partnerEmail);
		restaurant.setDeleted(true);
		repo.save(restaurant);
	}
}
