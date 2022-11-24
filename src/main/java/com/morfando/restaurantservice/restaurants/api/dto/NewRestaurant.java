package com.morfando.restaurantservice.restaurants.api.dto;

import com.morfando.restaurantservice.restaurants.model.entity.Address;
import com.morfando.restaurantservice.restaurants.model.entity.BusinessHours;
import com.morfando.restaurantservice.restaurants.model.entity.RestaurantType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class NewRestaurant {

	@NotBlank
	@Schema(example = "San Paolo")
	private String name;
	@NotNull
	@Schema(description = "Restaurant type")
	private RestaurantType type;
	@NotNull
	private Address address;
	@Min(1)
	@Max(4)
	@Schema(example = "2")
	private Integer priceRange;
	@Schema(description = "URLs of photos", nullable = true)
	private List<String> photos;
	@NotEmpty
	private List<BusinessHours> businessHours;

	public NewRestaurant(String name, RestaurantType type, Address address, Integer priceRange, List<String> photos, List<BusinessHours> businessHours) {
		this.name = name;
		this.type = type;
		this.address = address;
		this.priceRange = priceRange;
		this.photos = photos;
		this.businessHours = businessHours;
	}
}
