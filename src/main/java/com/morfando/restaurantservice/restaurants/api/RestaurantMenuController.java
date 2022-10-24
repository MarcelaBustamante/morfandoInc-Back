package com.morfando.restaurantservice.restaurants.api;

import com.morfando.restaurantservice.cross.dto.PaginatedResponse;
import com.morfando.restaurantservice.restaurants.api.dto.MenuItemFilters;
import com.morfando.restaurantservice.restaurants.api.dto.NewMenuItem;
import com.morfando.restaurantservice.restaurants.application.menu.CreateMenuItem;
import com.morfando.restaurantservice.restaurants.application.menu.DeleteMenuItem;
import com.morfando.restaurantservice.restaurants.application.menu.GetMenuItems;
import com.morfando.restaurantservice.restaurants.application.menu.UpdateMenuItem;
import com.morfando.restaurantservice.restaurants.model.entity.MenuItem;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants/{restaurantId}/menu/items")
public class RestaurantMenuController {

	private final GetMenuItems getMenuItems;
	private final CreateMenuItem createMenuItem;
	private final UpdateMenuItem updateMenuItem;
	private final DeleteMenuItem deleteMenuItem;

	public RestaurantMenuController(GetMenuItems getMenuItems, CreateMenuItem createMenuItem,
									UpdateMenuItem updateMenuItem, DeleteMenuItem deleteMenuItem) {
		this.getMenuItems = getMenuItems;
		this.createMenuItem = createMenuItem;
		this.updateMenuItem = updateMenuItem;
		this.deleteMenuItem = deleteMenuItem;
	}

	@GetMapping
	public PaginatedResponse<MenuItem> getMenuItems(@PathVariable("restaurantId") long restaurantId,
													@ParameterObject MenuItemFilters filters) {
		Page<MenuItem> page = getMenuItems.get(restaurantId, filters);
		return PaginatedResponse.from(page);
	}

	@GetMapping("/{itemId}")
	public MenuItem getMenuItem(@PathVariable("restaurantId") long restaurantId,
								@PathVariable("itemId") long itemId) {
		return getMenuItems.getById(restaurantId, itemId);
	}

	@PreAuthorize("hasAuthority('SCOPE_PARTNER')")
	@PostMapping
	public MenuItem createMenuItem(@PathVariable("restaurantId") long restaurantId, @RequestBody NewMenuItem item,
								   Authentication authentication) {
		return createMenuItem.create(restaurantId, item, authentication.getName());
	}

	@PreAuthorize("hasAuthority('SCOPE_PARTNER')")
	@PutMapping("/{itemId}")
	public MenuItem updateMenuItem(@PathVariable("restaurantId") long restaurantId,
								   @PathVariable("itemId") long itemId, @RequestBody NewMenuItem item,
								   Authentication authentication) {
		return updateMenuItem.update(restaurantId, itemId, item, authentication.getName());
	}

	@PreAuthorize("hasAuthority('SCOPE_PARTNER')")
	@DeleteMapping("/{itemId}")
	public void deleteMenuItem(@PathVariable("restaurantId") long restaurantId, @PathVariable("itemId") long itemId,
							   Authentication authentication) {
		deleteMenuItem.delete(restaurantId, itemId, authentication.getName());
	}
}
