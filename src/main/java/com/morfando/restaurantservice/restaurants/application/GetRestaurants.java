package com.morfando.restaurantservice.restaurants.application;

import com.morfando.restaurantservice.restaurants.model.RestaurantRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import com.morfando.restaurantservice.restaurants.api.dto.RestaurantFilters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class GetRestaurants {

	private final RestaurantRepository repo;

	public GetRestaurants(RestaurantRepository repo) {
		this.repo = repo;
	}

	public Page<Restaurant> get(RestaurantFilters filters) {
		Pageable pageable = PageRequest.of(filters.getPage(), filters.getPageSize());
		Specification<Restaurant> spec = repo.minRating(filters.getRating())
				.and(repo.type(filters.getType()))
				.and(repo.minPriceRange(filters.getMinPrice()))
				.and(repo.maxPriceRange(filters.getMaxPrice()));
		return repo.findAll(spec, pageable);
	}

	public Restaurant getById(long id) {
		return repo.findById(id).orElseThrow();
	}
}
