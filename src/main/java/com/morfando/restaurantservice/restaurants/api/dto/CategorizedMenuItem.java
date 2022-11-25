package com.morfando.restaurantservice.restaurants.api.dto;

import com.morfando.restaurantservice.restaurants.model.entity.MenuItem;
import lombok.Getter;

import java.util.List;

@Getter
public class CategorizedMenuItem {
	private final String category;
	private final List<MenuItem> items;

	public CategorizedMenuItem(String category, List<MenuItem> items) {
		this.category = category;
		this.items = items;
	}
}
