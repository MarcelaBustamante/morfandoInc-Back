package com.morfando.restaurantservice.restaurants.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MenuItemFilters {
	private Integer page = 0;
	private Integer pageSize = 20;
	private String category;
	private Boolean vegan;
	private Boolean tacc;
}
