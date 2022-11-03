package com.morfando.restaurantservice.restaurants.application.menu;

import com.morfando.restaurantservice.restaurants.api.dto.NewMenuItem;
import com.morfando.restaurantservice.restaurants.application.ValidateRestaurantOwner;
import com.morfando.restaurantservice.restaurants.model.MenuItemRepository;
import com.morfando.restaurantservice.restaurants.model.entity.MenuItem;
import org.springframework.stereotype.Service;

@Service
public class CreateMenuItem {

	private final MenuItemRepository repo;

	private final ValidateRestaurantOwner validateRestaurantOwner;

	public CreateMenuItem(MenuItemRepository repo, ValidateRestaurantOwner validateRestaurantOwner) {
		this.repo = repo;
		this.validateRestaurantOwner = validateRestaurantOwner;
	}

	public MenuItem create(long restaurantId, NewMenuItem newItem, String partnerEmail) {
		validateRestaurantOwner.validateOwnership(restaurantId, partnerEmail);
		double price = null != newItem.getPrice() ? newItem.getPrice() : 0.0D;
		boolean vegan = null != newItem.getVegan() && newItem.getVegan();
		boolean tacc = null == newItem.getTacc() || newItem.getTacc();
		MenuItem menuItem = new MenuItem(restaurantId, newItem.getType(), newItem.getName(), newItem.getDescription(),
				newItem.getCategory(), vegan, tacc, price, newItem.getPhoto());
		return repo.save(menuItem);
	}
}
