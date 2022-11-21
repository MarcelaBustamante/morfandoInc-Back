package com.morfando.restaurantservice.restaurants.api.dto;

import com.morfando.restaurantservice.restaurants.model.entity.Address;
import com.morfando.restaurantservice.restaurants.model.entity.BusinessHours;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Getter
public class RestaurantUpdate {

	@Schema(example = "San Paolo")
	private String name;
	@Schema(description = "Restaurant type. See GET /restaurants/types", example = "1")
	private Long type;
	private Address address;
	@Min(1)
	@Max(4)
	@Schema(example = "2")
	private Integer priceRange;
	@Schema(description = "URLs of photos", nullable = true)
	private List<String> photos;
	private List<BusinessHours> businessHours;
}
