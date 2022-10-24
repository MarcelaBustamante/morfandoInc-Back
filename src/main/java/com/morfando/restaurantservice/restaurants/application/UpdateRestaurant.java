package com.morfando.restaurantservice.restaurants.application;

import com.morfando.restaurantservice.restaurants.api.dto.NewRestaurant;
import com.morfando.restaurantservice.restaurants.api.dto.RestaurantUpdate;
import com.morfando.restaurantservice.restaurants.model.RestaurantRepository;
import com.morfando.restaurantservice.restaurants.model.RestaurantTypeRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import com.morfando.restaurantservice.restaurants.model.entity.RestaurantType;
import com.morfando.restaurantservice.users.application.FindUser;
import com.morfando.restaurantservice.users.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UpdateRestaurant {
	private final RestaurantRepository repo;
	private final RestaurantTypeRepository typeRepo;
	private final ValidateRestaurantOwner validateRestaurantOwner;

	public UpdateRestaurant(RestaurantRepository repo, RestaurantTypeRepository typeRepo, ValidateRestaurantOwner validateRestaurantOwner) {
		this.repo = repo;
		this.typeRepo = typeRepo;
		this.validateRestaurantOwner = validateRestaurantOwner;
	}

	public Restaurant update(long restaurantId, RestaurantUpdate update, String partnerEmail) {
		Restaurant restaurant = validateRestaurantOwner.validateOwnership(restaurantId, partnerEmail);
		if (null != update.getName()) {
			restaurant.setName(update.getName());
		}
		if (null != update.getPriceRange()) {
			restaurant.setPriceRange(update.getPriceRange());
		}
		if (null != update.getType()) {
			RestaurantType type = typeRepo.findById(update.getType())
					.orElseThrow(() -> new IllegalArgumentException("Invalid restaurant type "+ update.getType()));
			restaurant.setType(type);
		}
		if (null != update.getAddress()) {
			restaurant.setAddress(update.getAddress());
		}
		if (null != update.getPhotos()) {
			restaurant.updatePhotos(update.getPhotos());
		}
		if (null != update.getBusinessHours()) {
			restaurant.updateBusinessHours(update.getBusinessHours());
		}
		return repo.save(restaurant);
	}
}
