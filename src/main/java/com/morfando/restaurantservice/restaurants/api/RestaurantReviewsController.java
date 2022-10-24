package com.morfando.restaurantservice.restaurants.api;

import com.morfando.restaurantservice.cross.dto.PaginatedResponse;
import com.morfando.restaurantservice.restaurants.api.dto.NewReview;
import com.morfando.restaurantservice.restaurants.api.dto.ReviewFilters;
import com.morfando.restaurantservice.restaurants.application.reviews.CreateReview;
import com.morfando.restaurantservice.restaurants.application.reviews.DeleteReview;
import com.morfando.restaurantservice.restaurants.application.reviews.GetReviews;
import com.morfando.restaurantservice.restaurants.model.entity.Review;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/restaurants/{restaurantId}/reviews")
public class RestaurantReviewsController {

	private final GetReviews getReviews;
	private final CreateReview createReview;
	private final DeleteReview deleteReview;

	public RestaurantReviewsController(GetReviews getReviews, CreateReview createReview, DeleteReview deleteReview) {
		this.getReviews = getReviews;
		this.createReview = createReview;
		this.deleteReview = deleteReview;
	}

	@GetMapping
	public PaginatedResponse<Review> getReviews(@PathVariable("restaurantId") long restaurantId,
												@ParameterObject ReviewFilters filters) {
		Page<Review> page = getReviews.get(restaurantId, filters);
		return PaginatedResponse.from(page);
	}

	@GetMapping("/{reviewId}")
	public Review getReview(@PathVariable("restaurantId") long restaurantId, @PathVariable("reviewId") long reviewId) {
		return getReviews.getById(restaurantId, reviewId);
	}

	@PostMapping
	public Review createReview(@PathVariable("restaurantId") long restaurantId, @RequestBody @Valid NewReview newReview,
							   Authentication authentication) {
		return createReview.create(restaurantId, newReview, authentication.getName());
	}

	@DeleteMapping("/{reviewId}")
	public void deleteReview(@PathVariable("restaurantId") long restaurantId, @PathVariable("reviewId") long reviewId,
							 Authentication authentication) {
		deleteReview.delete(restaurantId, reviewId, authentication.getName());
	}

}
