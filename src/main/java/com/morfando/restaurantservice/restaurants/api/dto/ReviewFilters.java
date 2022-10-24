package com.morfando.restaurantservice.restaurants.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewFilters {
	private Integer page = 0;
	private Integer pageSize = 20;
	private Integer rating;
	private Long userId;
}
