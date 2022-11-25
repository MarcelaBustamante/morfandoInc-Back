package com.morfando.restaurantservice.restaurants.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class MenuItem {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_menu_item")
	private Long id;

	@Column(nullable = false)
	private long restaurantId;

	@Enumerated(EnumType.STRING)
	private MenuItemType type;
	private String category;
	private String name;
	private String description;
	private boolean vegan;
	private boolean tacc;

	private double price;
	private String photo;

	public MenuItem(long restaurantId, MenuItemType type, String name, String description, String category,
					boolean vegan, boolean tacc, double price, String photo) {
		this.restaurantId = restaurantId;
		this.type = type;
		this.name = name;
		this.description = description;
		this.category = formatCategory(category);
		this.vegan = vegan;
		this.tacc = tacc;
		this.price = price;
		this.photo = photo;
	}

	private String formatCategory(String category) {
		if (null == category) {
			return "";
		}
		category = category.trim().toLowerCase();
		if (category.length() > 0) {
			category = category.substring(0, 1).toUpperCase() + category.substring(1);
		}
		return category;
	}
}
