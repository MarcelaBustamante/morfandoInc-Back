package com.morfando.restaurantservice.restaurants.application.reviews;

import com.morfando.restaurantservice.restaurants.model.ReviewRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Review;
import com.morfando.restaurantservice.users.application.FindUser;
import com.morfando.restaurantservice.users.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteReview {

	private final ReviewRepository repo;
	private final FindUser findUser;

	public DeleteReview(ReviewRepository repo, FindUser findUser) {
		this.repo = repo;
		this.findUser = findUser;
	}

	@Transactional
	public void delete(long restaurantId, long reviewId, String userEmail) {
		User user = findUser.find(userEmail);
		repo.deleteByIdAndRestaurantIdAndUserId(reviewId, restaurantId, user.getId());
	}
}
