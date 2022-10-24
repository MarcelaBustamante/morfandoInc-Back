package com.morfando.restaurantservice.restaurants.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_review")
	private Long id;
	private String comment;
	private int rating;

	private long restaurantId;
	private long userId;

	public Review(String comment, int rating, long restaurantId, long userId) {
		this.comment = comment;
		this.rating = rating;
		this.restaurantId = restaurantId;
		this.userId = userId;
	}
}
