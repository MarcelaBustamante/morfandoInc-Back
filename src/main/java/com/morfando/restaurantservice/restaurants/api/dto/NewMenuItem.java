package com.morfando.restaurantservice.restaurants.api.dto;

import com.morfando.restaurantservice.restaurants.model.entity.MenuItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewMenuItem {
	private MenuItemType type;
	@Schema(example = "hot dishes")
	private String category;
	@Schema(example = "Goulash", required = true)
	private String name;
	@Schema(example = "Goulash")
	private String description;
	@Schema(defaultValue = "false")
	private Boolean vegan;
	@Schema(defaultValue = "true")
	private Boolean tacc;
	@Schema(example = "100.5", required = true)
	private Double price;
	@Schema(example = "https://image/image.png")
	private String photo;
}
