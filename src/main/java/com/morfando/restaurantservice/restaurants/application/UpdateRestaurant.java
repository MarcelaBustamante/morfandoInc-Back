package com.morfando.restaurantservice.restaurants.application;

import com.morfando.restaurantservice.restaurants.api.dto.RestaurantUpdate;
import com.morfando.restaurantservice.restaurants.model.RestaurantRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import com.morfando.restaurantservice.restaurants.model.entity.RestaurantType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateRestaurant {
	private final RestaurantRepository repo;
	private final ValidateRestaurantOwner validateRestaurantOwner;

	public UpdateRestaurant(RestaurantRepository repo, ValidateRestaurantOwner validateRestaurantOwner) {
		this.repo = repo;
		this.validateRestaurantOwner = validateRestaurantOwner;
	}

	@Transactional
	public Restaurant update(long restaurantId, RestaurantUpdate update, String partnerEmail) {
		Restaurant restaurant = validateRestaurantOwner.validateOwnership(restaurantId, partnerEmail);
		if (null != update.getName()) {
			restaurant.setName(update.getName());
		}
		if (null != update.getPriceRange()) {
			restaurant.setPriceRange(update.getPriceRange());
		}
		if (null != update.getType()) {
			restaurant.setType(update.getType());
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
		if (null != update.getActive()) {
			restaurant.setActive(update.getActive());
		}
		return repo.save(restaurant);
	}
}
