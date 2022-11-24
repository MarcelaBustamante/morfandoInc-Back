package com.morfando.restaurantservice.restaurants.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

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
	//@Column(precision = 12, scale = 8)
	@NotNull
	private Double latitude;
	@Schema(example = "0.01")
	private Double latitudeDelta;
	@NotNull
	private Double longitude;
	@Schema(example = "0.01")
	private Double longitudeDelta;
}
