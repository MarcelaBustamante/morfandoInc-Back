package com.morfando.restaurantservice.restaurants.application;

import com.morfando.restaurantservice.restaurants.api.dto.NewRestaurant;
import com.morfando.restaurantservice.restaurants.model.RestaurantRepository;
import com.morfando.restaurantservice.restaurants.model.RestaurantTypeRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Photo;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import com.morfando.restaurantservice.restaurants.model.entity.RestaurantType;
import com.morfando.restaurantservice.users.application.FindUser;
import com.morfando.restaurantservice.users.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class CreateRestaurant {
	private final RestaurantRepository repo;
	private final RestaurantTypeRepository typeRepo;
	private final FindUser findUser;

	public CreateRestaurant(RestaurantRepository repo, RestaurantTypeRepository typeRepo, FindUser findUser) {
		this.repo = repo;
		this.typeRepo = typeRepo;
		this.findUser = findUser;
	}

	public Restaurant create(NewRestaurant newRestaurant, String partnerEmail) {
		User owner = findUser.find(partnerEmail);
		RestaurantType type = typeRepo.findById(newRestaurant.getType())
				.orElseThrow(() -> new IllegalArgumentException("Invalid restaurant type "+ newRestaurant.getType()));
		Restaurant restaurant = new Restaurant(newRestaurant.getName(), owner.getId(), type, newRestaurant.getAddress(),
				newRestaurant.getPriceRange());
		if (null != newRestaurant.getPhotos()) {
			newRestaurant.getPhotos().forEach(restaurant::addPhoto);
		}
		if (null != newRestaurant.getBusinessHours()) {
			newRestaurant.getBusinessHours().forEach(restaurant::addBusinessHours);
		}
		return repo.save(restaurant);
	}
}
