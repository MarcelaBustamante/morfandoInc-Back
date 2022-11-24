package com.morfando.restaurantservice.restaurants.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Restaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_restaurant")
	private Long id;
	@Column(unique = true)
	private String name;

	@Transient
	private RestaurantStatus status;
	@Enumerated(EnumType.STRING)
	private RestaurantType type;
	@Embedded
	private Address address;
	private int rating;
	private int priceRange;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BusinessHours> businessHours = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Photo> photos = new ArrayList<>();
	private long owner;

	public Restaurant(String name, long owner, RestaurantType type, Address address, int priceRange) {
		this.name = name;
		this.type = type;
		this.address = address;
		this.priceRange = priceRange;
		this.owner = owner;
	}

	public void addPhoto(String url) {
		Photo photo = new Photo(url);
		this.photos.add(photo);
		photo.setRestaurant(this);
	}

	public void updatePhotos(List<String> photos) {
		this.photos.forEach(p -> p.setRestaurant(null));
		this.photos.clear();
		photos.forEach(this::addPhoto);
	}

	public void addBusinessHours(BusinessHours hours) {
		this.businessHours.add(hours);
		hours.setRestaurant(this);
	}

	public void updateBusinessHours(List<BusinessHours> hours) {
		this.businessHours.forEach(bh -> bh.setRestaurant(null));
		hours.forEach(this::addBusinessHours);
	}

	public RestaurantStatus getStatus() {
		if (null == businessHours || businessHours.isEmpty()) {
			return RestaurantStatus.CLOSED;
		}
		LocalDateTime now = LocalDateTime.now();
		boolean open = this.businessHours.stream()
				.filter(bh -> now.getDayOfWeek().equals(bh.getDayOfWeek()))
				.anyMatch(bh -> bh.getFromTime().isBefore(now.toLocalTime())
						&& bh.getToTime().isAfter(now.toLocalTime()));
		return open ? RestaurantStatus.OPEN : RestaurantStatus.CLOSED;
	}
}
