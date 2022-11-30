package com.morfando.restaurantservice.restaurants.application;

import com.morfando.restaurantservice.restaurants.model.RestaurantRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import com.morfando.restaurantservice.restaurants.api.dto.RestaurantFilters;
import com.morfando.restaurantservice.users.application.FindUser;
import com.morfando.restaurantservice.users.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class GetRestaurants {

	private final RestaurantRepository repo;
	private final FindUser findUser;
	private final Environment env;

	public Page<Restaurant> get(RestaurantFilters filters) {
		Pageable pageable = PageRequest.of(filters.getPage(), filters.getPageSize());
		String search = null !=  filters.getSearch() ? "%" +  filters.getSearch().toLowerCase() + "%" : null;
		if (null != filters.getLatitude() && null != filters.getLongitude()) {
			Sort sort = Sort.by(Sort.Direction.ASC, "DISTANCE").and(pageable.getSort());
			pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		} else {
			filters.setLongitude(0.0D);
			filters.setLatitude(0.0D);
		}
		return repo.findAllWithFilters(filters.getType(), filters.getMinPrice(), filters.getMaxPrice(),
				filters.getRating(), filters.getLatitude(), filters.getLongitude(), search, filters.getRadius(), pageable);
	}

	public Restaurant getById(long id) {
		return repo.findById(id).orElseThrow();
	}

	public List<Restaurant> getMyRestaurants(String email) {
		User owner = findUser.find(email);
		return repo.findByOwnerAndDeletedFalse(owner.getId());
	}
}
