package com.morfando.restaurantservice.restaurants.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class RestaurantType {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_restaurant_type")
	private Long id;
	private String name;
}
