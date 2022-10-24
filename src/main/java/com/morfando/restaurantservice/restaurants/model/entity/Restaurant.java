package com.morfando.restaurantservice.restaurants.model.entity;

import com.morfando.restaurantservice.restaurants.api.dto.NewRestaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
	@Enumerated(EnumType.STRING)
	private RestaurantStatus status;
	@ManyToOne
	@JoinColumn(name = "type_id")
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

//	public void removePhoto(Photo photo) {
//		this.photos.remove(photo);
//		photo.setRestaurant(null);
//	}

	public void updatePhotos(List<String> photos) {
		this.photos.forEach(p -> p.setRestaurant(null));
		this.photos.clear();
		photos.forEach(this::addPhoto);
	}

	public void addBusinessHours(BusinessHours hours) {
		this.businessHours.add(hours);
		hours.setRestaurant(this);
	}

//	public void removeBusinessHours(BusinessHours hours) {
//		this.businessHours.remove(hours);
//		hours.setRestaurant(null);
//	}

	public void updateBusinessHours(List<BusinessHours> hours) {
		this.businessHours.forEach(bh -> bh.setRestaurant(null));
		hours.forEach(this::addBusinessHours);
	}
}
