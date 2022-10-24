package com.morfando.restaurantservice.restaurants.api;

import com.morfando.restaurantservice.restaurants.model.RestaurantTypeRepository;
import com.morfando.restaurantservice.restaurants.model.entity.RestaurantType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurants/types")
public class RestaurantTypesController {

	private RestaurantTypeRepository repo;

	public RestaurantTypesController(RestaurantTypeRepository repo) {
		this.repo = repo;
	}

	@GetMapping
	public List<RestaurantType> getRestaurantTypes() {
		return repo.findAll();
	}
}
