package com.morfando.restaurantservice.restaurants.api;

import com.morfando.restaurantservice.cross.dto.PaginatedResponse;
import com.morfando.restaurantservice.restaurants.api.dto.NewRestaurant;
import com.morfando.restaurantservice.restaurants.api.dto.RestaurantUpdate;
import com.morfando.restaurantservice.restaurants.application.DeleteRestaurant;
import com.morfando.restaurantservice.restaurants.application.UpdateRestaurant;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import com.morfando.restaurantservice.restaurants.api.dto.RestaurantFilters;
import com.morfando.restaurantservice.restaurants.application.CreateRestaurant;
import com.morfando.restaurantservice.restaurants.application.GetRestaurants;
import lombok.extern.slf4j.Slf4j;
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

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/restaurants")
public class RestaurantsController {

	private final GetRestaurants getRestaurants;
	private final CreateRestaurant createRestaurant;
	private final UpdateRestaurant updateRestaurant;
	private final DeleteRestaurant deleteRestaurant;

	public RestaurantsController(GetRestaurants getRestaurants, CreateRestaurant createRestaurant, UpdateRestaurant updateRestaurant, DeleteRestaurant deleteRestaurant) {
		this.getRestaurants = getRestaurants;
		this.createRestaurant = createRestaurant;
		this.updateRestaurant = updateRestaurant;
		this.deleteRestaurant = deleteRestaurant;
	}

	@GetMapping
	public PaginatedResponse<Restaurant> getRestaurants(@ParameterObject RestaurantFilters filters) {
		Page<Restaurant> page = getRestaurants.get(filters);
		return PaginatedResponse.from(page);
	}

	@PreAuthorize("hasAuthority('SCOPE_PARTNER')")
	@GetMapping("/me")
	public List<Restaurant> getRestaurants(Authentication authentication) {
		return getRestaurants.getMyRestaurants(authentication.getName());
	}

	@GetMapping("/{id}")
	public Restaurant getRestaurant(@PathVariable("id") long id) {
		return getRestaurants.getById(id);
	}

	@PreAuthorize("hasAuthority('SCOPE_PARTNER')")
	@PostMapping
	public Restaurant createRestaurant(@RequestBody @Valid NewRestaurant newRestaurant, Authentication authentication) {
		return createRestaurant.create(newRestaurant, authentication.getName());
	}

	@PreAuthorize("hasAuthority('SCOPE_PARTNER')")
	@PutMapping("/{id}")
	public Restaurant updateRestaurant(@PathVariable("id") long id, @RequestBody @Valid RestaurantUpdate update,
									   Authentication authentication) {
		return updateRestaurant.update(id, update, authentication.getName());
	}

	@PreAuthorize("hasAuthority('SCOPE_PARTNER')")
	@DeleteMapping("/{id}")
	public void deleteRestaurant(@PathVariable("id") long id, Authentication authentication) {
		deleteRestaurant.delete(id, authentication.getName());
	}
}
