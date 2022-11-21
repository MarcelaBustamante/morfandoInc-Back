package com.morfando.restaurantservice.restaurants.model;

import com.morfando.restaurantservice.restaurants.model.entity.Review;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

	Review findByIdAndRestaurantId(long reviewId, long restaurantId);

	void deleteByIdAndRestaurantIdAndUserId(long reviewId, long restaurantId, Long id);

	default Specification<Review> restaurantId(long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("restaurantId"), id);
	}

	default Specification<Review> rating(Integer rating) {
		return (root, query, criteriaBuilder) -> {
			if (null == rating ) return null;
			return criteriaBuilder.equal(root.get("rating"), rating);
		};
	}

	default Specification<Review> userId(Long userId) {
		return (root, query, criteriaBuilder) -> {
			if (null == userId ) return null;
			return criteriaBuilder.equal(root.get("userId"), userId);
		};
	}
}
