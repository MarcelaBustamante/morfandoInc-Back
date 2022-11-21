package com.morfando.restaurantservice.restaurants.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	@Schema(example = "Lima")
	private String street;
	@Schema(example = "775")
	private String number;
	@Schema(example = "Montserrat")
	private String neighborhood;
	@Schema(example = "CABA")
	private String city;
	@Schema(example = "CABA")
	private String province;
	@Schema(example = "C1073AAO")
	private String zipCode;
}
