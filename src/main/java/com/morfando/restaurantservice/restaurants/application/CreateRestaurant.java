package com.morfando.restaurantservice.restaurants.application;

import com.morfando.restaurantservice.restaurants.api.dto.NewRestaurant;
import com.morfando.restaurantservice.restaurants.model.RestaurantRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import com.morfando.restaurantservice.users.application.FindUser;
import com.morfando.restaurantservice.users.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateRestaurant {
	private final RestaurantRepository repo;
	private final FindUser findUser;

	public CreateRestaurant(RestaurantRepository repo, FindUser findUser) {
		this.repo = repo;
		this.findUser = findUser;
	}

	@Transactional
	public Restaurant create(NewRestaurant newRestaurant, String partnerEmail) {
		User owner = findUser.find(partnerEmail);
		Restaurant restaurant = new Restaurant(newRestaurant.getName(), owner.getId(), newRestaurant.getType(),
				newRestaurant.getAddress(),	newRestaurant.getPriceRange(), newRestaurant.isActive());
		if (null != newRestaurant.getPhotos()) {
			newRestaurant.getPhotos().forEach(restaurant::addPhoto);
		}
		if (null != newRestaurant.getBusinessHours()) {
			newRestaurant.getBusinessHours().forEach(restaurant::addBusinessHours);
		}
		return repo.save(restaurant);
	}
}
