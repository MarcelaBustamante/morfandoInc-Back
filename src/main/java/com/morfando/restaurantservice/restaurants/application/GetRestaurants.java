package com.morfando.restaurantservice.restaurants.application;

import com.morfando.restaurantservice.restaurants.model.RestaurantRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import com.morfando.restaurantservice.restaurants.api.dto.RestaurantFilters;
import com.morfando.restaurantservice.users.application.FindUser;
import com.morfando.restaurantservice.users.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRestaurants {

	private final RestaurantRepository repo;

	private final FindUser findUser;

	public GetRestaurants(RestaurantRepository repo, FindUser findUser) {
		this.repo = repo;
		this.findUser = findUser;
	}

	public Page<Restaurant> get(RestaurantFilters filters) {
		Pageable pageable = PageRequest.of(filters.getPage(), filters.getPageSize());
		if (null != filters.getLatitude() && null != filters.getLongitude()) {
			Sort sort = Sort.by(Sort.Direction.ASC, "DIST").and(pageable.getSort());
			pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		} else {
			filters.setLongitude(0.0);
			filters.setLatitude(0.0);
		}
		return repo.findAllWithFilters(filters.getType(), filters.getMinPrice(), filters.getMaxPrice(),
				filters.getRating(), filters.getLatitude(), filters.getLongitude(), pageable);
	}

	public Restaurant getById(long id) {
		return repo.findById(id).orElseThrow();
	}

	public List<Restaurant> getMyRestaurants(String email) {
		User owner = findUser.find(email);
		return repo.findByOwner(owner.getId());
	}
}
