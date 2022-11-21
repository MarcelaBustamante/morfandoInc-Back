package com.morfando.restaurantservice.restaurants.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestaurantFilters {
	private Integer page = 0;
	private Integer pageSize = 20;
	private String latitude;
	private String longitude;
	private Integer radius = 10;
	private String type;
	private Integer rating;
	private Integer minPrice;
	private Integer maxPrice;
}
