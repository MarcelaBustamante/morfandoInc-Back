package com.morfando.restaurantservice.restaurants.application.menu;

import com.morfando.restaurantservice.restaurants.application.ValidateRestaurantOwner;
import com.morfando.restaurantservice.restaurants.model.MenuItemRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteMenuItem {

	private final MenuItemRepository repo;
	private final ValidateRestaurantOwner validateRestaurantOwner;

	public DeleteMenuItem(MenuItemRepository repo, ValidateRestaurantOwner validateRestaurantOwner) {
		this.repo = repo;
		this.validateRestaurantOwner = validateRestaurantOwner;
	}

	public void delete(long restaurantId, long menuItemId, String partnerEmail) {
		validateRestaurantOwner.validateOwnership(restaurantId, partnerEmail);
		repo.deleteByIdAndRestaurantId(menuItemId, restaurantId);
	}
}
