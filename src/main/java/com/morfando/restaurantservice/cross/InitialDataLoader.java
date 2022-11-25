package com.morfando.restaurantservice.cross;

import com.morfando.restaurantservice.restaurants.api.dto.NewMenuItem;
import com.morfando.restaurantservice.restaurants.api.dto.NewRestaurant;
import com.morfando.restaurantservice.restaurants.application.CreateRestaurant;
import com.morfando.restaurantservice.restaurants.application.menu.CreateMenuItem;
import com.morfando.restaurantservice.restaurants.model.MenuItemRepository;
import com.morfando.restaurantservice.restaurants.model.RestaurantRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Address;
import com.morfando.restaurantservice.restaurants.model.entity.BusinessHours;
import com.morfando.restaurantservice.restaurants.model.entity.MenuItemType;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import com.morfando.restaurantservice.restaurants.model.entity.RestaurantType;
import com.morfando.restaurantservice.users.application.CreateUser;
import com.morfando.restaurantservice.users.model.UserRepository;
import com.morfando.restaurantservice.users.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Component
public class InitialDataLoader {

	private final UserRepository userRepository;
	private final CreateUser createUser;
	private final CreateRestaurant createRestaurant;
	private final CreateMenuItem createMenuItem;

	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void createSomeRestaurants() {
		if (userRepository.count() != 0) {
			return;
		}
		User user = createUser.createPartner("morfandoapp@mailinator.com", "123", "Usuario",
				"Test", null);
		Address address = new Address("Uriarte", "1616", "Palermo", "CABA", "CABA",
				-34.5871469, 0.01, -58.4320105, 0.01);
		List<BusinessHours> hours = Arrays.asList(
			new BusinessHours(DayOfWeek.MONDAY, LocalTime.of(12, 15), LocalTime.of(23, 0)),
			new BusinessHours(DayOfWeek.TUESDAY, LocalTime.of(12, 15), LocalTime.of(23, 0)),
			new BusinessHours(DayOfWeek.THURSDAY, LocalTime.of(12, 15), LocalTime.of(23, 0)),
			new BusinessHours(DayOfWeek.FRIDAY, LocalTime.of(11, 30), LocalTime.of(23, 30))
		);
		List<String> images = Arrays.asList(
			"https://media-cdn.tripadvisor.com/media/photo-s/0d/67/51/6b/photo1jpg.jpg",
			"https://media-cdn.tripadvisor.com/media/photo-s/0a/43/3f/b7/logo.jpg"
		);
		NewRestaurant newRestaurant = new NewRestaurant(
				"San Paolo", RestaurantType.ITALIANA, address, 1, images, hours);

		Restaurant rest1 = createRestaurant.create(newRestaurant, user.getEmail());
		NewMenuItem newItem1 = new NewMenuItem();
		newItem1.setName("Margherita");
		newItem1.setType(MenuItemType.DISH);
		newItem1.setCategory("Pizzas");
		newItem1.setDescription("Pizza con tomates pelados, mozzarella fresca y albahaca");
		newItem1.setPhoto("https://media-cdn.tripadvisor.com/media/photo-s/0b/bd/8d/1d/photo0jpg.jpg");
		newItem1.setPrice(1500D);
		newItem1.setVegan(false);
		newItem1.setTacc(true);

		NewMenuItem newItem2 = new NewMenuItem();
		newItem1.setName("Aperol Spritz");
		newItem1.setType(MenuItemType.DISH);
		newItem1.setCategory("Tragos");
		newItem1.setDescription("Aperol Spritz");
		newItem1.setPhoto("https://img.restaurantguru.com/ra7a-beer-San-Paolo-Pizzeria-2022-10.jpg");
		newItem1.setPrice(850D);
		newItem1.setVegan(true);
		newItem1.setTacc(false);

		createMenuItem.create(rest1.getId(), newItem1, user.getEmail());
		createMenuItem.create(rest1.getId(), newItem2, user.getEmail());

		Address address2 = new Address("Gorriti", "4389", "Palermo", "CABA", "CABA",
				-34.5935282, 0.01, -58.4249135, 0.01);
		List<BusinessHours> hours2 = Arrays.asList(
				new BusinessHours(DayOfWeek.MONDAY, LocalTime.of(12, 15), LocalTime.of(23, 0)),
				new BusinessHours(DayOfWeek.TUESDAY, LocalTime.of(12, 15), LocalTime.of(23, 0)),
				new BusinessHours(DayOfWeek.FRIDAY, LocalTime.of(11, 30), LocalTime.of(23, 30)),
				new BusinessHours(DayOfWeek.SATURDAY, LocalTime.of(11, 30), LocalTime.of(23, 0))
		);
		List<String> images2 = Arrays.asList(
				"https://media-cdn.tripadvisor.com/media/photo-s/08/03/4a/69/nola.jpg",
				"https://media-cdn.tripadvisor.com/media/photo-s/16/54/65/1d/photo5jpg.jpg"
		);
		NewRestaurant newRestaurant2 = new NewRestaurant(
				"Nola", RestaurantType.NORTEAMERICANA, address2, 1, images2, hours2);

		Restaurant rest2 = createRestaurant.create(newRestaurant2, user.getEmail());

		NewMenuItem newItem3 = new NewMenuItem();
		newItem1.setName("Pechuga");
		newItem1.setType(MenuItemType.DISH);
		newItem1.setCategory("Sandwichs");
		newItem1.setDescription("Sandwich de pechuga de pollo frito");
		newItem1.setPhoto("https://media-cdn.tripadvisor.com/media/photo-s/11/a5/d4/c4/photo0jpg.jpg");
		newItem1.setPrice(1200D);
		newItem1.setVegan(false);
		newItem1.setTacc(true);

		NewMenuItem newItem4 = new NewMenuItem();
		newItem1.setName("Aperol Spritz");
		newItem1.setType(MenuItemType.DISH);
		newItem1.setCategory("Bebidas");
		newItem1.setDescription("Aperol Spritz");
		newItem1.setPhoto("https://img.restaurantguru.com/ra7a-beer-San-Paolo-Pizzeria-2022-10.jpg");
		newItem1.setPrice(820D);
		newItem1.setVegan(true);
		newItem1.setTacc(false);

		createMenuItem.create(rest2.getId(), newItem3, user.getEmail());
		createMenuItem.create(rest2.getId(), newItem4, user.getEmail());
	}
}
