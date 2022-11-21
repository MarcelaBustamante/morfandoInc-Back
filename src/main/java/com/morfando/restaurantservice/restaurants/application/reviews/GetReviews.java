package com.morfando.restaurantservice.restaurants.application.reviews;

import com.morfando.restaurantservice.restaurants.api.dto.ReviewFilters;
import com.morfando.restaurantservice.restaurants.model.ReviewRepository;
import com.morfando.restaurantservice.restaurants.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class GetReviews {
	private final ReviewRepository repo;

	public GetReviews(ReviewRepository repo) {
		this.repo = repo;
	}

	public Page<Review> get(long restaurantId, ReviewFilters filters) {
		Pageable pageable = PageRequest.of(filters.getPage(), filters.getPageSize());
		Specification<Review> spec = repo.restaurantId(restaurantId)
				.and(repo.rating(filters.getRating()))
				.and((repo.userId(filters.getUserId())));
		return repo.findAll(spec, pageable);
	}

	public Review getById(long restaurantId, long reviewId) {
		return repo.findByIdAndRestaurantId(reviewId, restaurantId);
	}
}
