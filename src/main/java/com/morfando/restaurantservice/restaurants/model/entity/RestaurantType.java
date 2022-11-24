package com.morfando.restaurantservice.restaurants.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public enum RestaurantType {
	ALEMANA,
	ARGENTINA,
	ARMENIA,
	CHINA,
	INDIA,
	JAPONESA,
	NORTEAMERICANA,
	PERUANA
}
