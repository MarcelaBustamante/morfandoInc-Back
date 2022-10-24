package com.morfando.restaurantservice.restaurants.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class NewReview {
	@Size(max = 2048)
	@NotBlank
	private String comment;
	@Min(1)
	@Max(5)
	private int rating;
}
