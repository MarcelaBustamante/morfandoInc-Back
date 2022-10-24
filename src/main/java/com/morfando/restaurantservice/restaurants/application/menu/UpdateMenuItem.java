package com.morfando.restaurantservice.restaurants.application.menu;

import com.morfando.restaurantservice.restaurants.api.dto.NewMenuItem;
import com.morfando.restaurantservice.restaurants.application.ValidateRestaurantOwner;
import com.morfando.restaurantservice.restaurants.model.MenuItemRepository;
import com.morfando.restaurantservice.restaurants.model.entity.MenuItem;
import org.springframework.stereotype.Service;

@Service
public class UpdateMenuItem {

	private final MenuItemRepository repo;
	private final ValidateRestaurantOwner validateRestaurantOwner;

	public UpdateMenuItem(MenuItemRepository repo, ValidateRestaurantOwner validateRestaurantOwner) {
		this.repo = repo;
		this.validateRestaurantOwner = validateRestaurantOwner;
	}

	public MenuItem update(long restaurantId, long itemId, NewMenuItem newItem, String partnerEmail) {
		validateRestaurantOwner.validateOwnership(restaurantId, partnerEmail);
		MenuItem menuItem = repo.findByIdAndRestaurantId(restaurantId, itemId);
		if (null != newItem.getName()) {
			menuItem.setName(newItem.getName());
		}
		if (null != newItem.getDescription()) {
			menuItem.setDescription(newItem.getDescription());
		}
		if (null != newItem.getType()) {
			menuItem.setType(newItem.getType());
		}
		if (null != newItem.getCategory()) {
			menuItem.setCategory(newItem.getCategory());
		}
		if (null != newItem.getPhoto()) {
			menuItem.setPhoto(newItem.getPhoto());
		}
		if (null != newItem.getTacc()) {
			menuItem.setTacc(null == newItem.getTacc() || newItem.getTacc());
		}
		if (null != newItem.getVegan()) {
			menuItem.setVegan(null != newItem.getVegan() && newItem.getVegan());
		}
		return repo.save(menuItem);
	}
}
