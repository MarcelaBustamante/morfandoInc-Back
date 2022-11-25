package com.morfando.restaurantservice.restaurants.application.menu;

import com.morfando.restaurantservice.restaurants.api.dto.MenuItemFilters;
import com.morfando.restaurantservice.restaurants.model.MenuItemRepository;
import com.morfando.restaurantservice.restaurants.model.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetMenuItems {
	private final MenuItemRepository repo;

	public GetMenuItems(MenuItemRepository repo) {
		this.repo = repo;
	}

	public List<MenuItem> get(long restaurantId, MenuItemFilters filters) {
		Specification<MenuItem> spec = repo.restaurantId(restaurantId)
				.and(repo.vegan(filters.getVegan()))
				.and(repo.tacc(filters.getTacc()))
				.and(repo.categoryLike(filters.getCategory()));
		return repo.findAll(spec);
	}

	public MenuItem getById(long restaurantId, long menuItemId) {
		return repo.findByIdAndRestaurantId(menuItemId, restaurantId);
	}
}
