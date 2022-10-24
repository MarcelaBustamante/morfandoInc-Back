package com.morfando.restaurantservice.restaurants.application.reviews;

import com.morfando.restaurantservice.restaurants.api.dto.NewReview;
import com.morfando.restaurantservice.restaurants.model.ReviewRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Review;
import com.morfando.restaurantservice.users.application.FindUser;
import com.morfando.restaurantservice.users.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class CreateReview {
	private final ReviewRepository repo;
	private final FindUser findUser;

	public CreateReview(ReviewRepository repo, FindUser findUser) {
		this.repo = repo;
		this.findUser = findUser;
	}
	public Review create(long restaurantId, NewReview newReview, String userEmail) {
		User user = findUser.find(userEmail);
		return repo.save(new Review(newReview.getComment(), newReview.getRating(), restaurantId, user.getId()));
	}
}
