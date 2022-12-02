package com.morfando.restaurantservice.users.application;

import org.springframework.stereotype.Service;
import com.morfando.restaurantservice.users.model.UserRepository;
import com.morfando.restaurantservice.users.model.entity.User;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;

@Service
public class HandleFavourite {
    private final UserRepository repo;

	public HandleFavourite(UserRepository repo) {
		this.repo = repo;
	}

    public void addFavourite(long idUser,Restaurant restaurant){
		User user = repo.findById(idUser).orElseThrow();
		user.getFavourites().add(restaurant);
		repo.save(user);
	}

	public void deleteFavourite(long idUser,Restaurant restaurant){
		User user = repo.findById(idUser).orElseThrow();
		user.getFavourites().remove(restaurant);
		repo.save(user);
	}

	public boolean getFavourite(long idUser,Restaurant restaurant){
		User user = repo.findById(idUser).orElseThrow();
		boolean exists = false;
		for(Restaurant resto : user.getFavourites()) {
			if(resto.getId() == restaurant.getId()){
				exists=true;
				break;
			}
		}
		return exists;

	}
}
