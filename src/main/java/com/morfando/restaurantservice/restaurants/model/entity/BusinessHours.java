package com.morfando.restaurantservice.restaurants.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class BusinessHours {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_business_hours")
	private Long id;
	@NotNull
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;
	@NotNull
	private LocalTime fromTime;
	@NotNull
	private LocalTime toTime;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	public BusinessHours(DayOfWeek dayOfWeek, LocalTime fromTime, LocalTime toTime) {
		this.dayOfWeek = dayOfWeek;
		this.fromTime = fromTime;
		this.toTime = toTime;
	}
}
