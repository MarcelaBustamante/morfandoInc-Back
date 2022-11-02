package com.morfando.restaurantservice.restaurants.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "url")
public class Photo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_photo")
	private Long id;
	@Column(length = 2048)
	private String url;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	public Photo(String url) {
		this.url = url;
	}
}
