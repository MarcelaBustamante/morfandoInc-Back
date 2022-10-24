package com.morfando.restaurantservice.restaurants.model;

import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {

	default Specification<Restaurant> minRating(Integer rating) {
		return (root, query, criteriaBuilder) -> {
			if (null == rating ) return null;
			return criteriaBuilder.ge(root.get("rating"), rating);
		};
	}

	default Specification<Restaurant> type(String type) {
		return (root, query, criteriaBuilder) -> {
			if (ObjectUtils.isEmpty(type)) return null;
			return criteriaBuilder.equal(root.get("type.name"), type);
		};
	}

	default Specification<Restaurant> minPriceRange(Integer minPrice) {
		return (root, query, criteriaBuilder) -> {
			if (null == minPrice ) return null;
			return criteriaBuilder.ge(root.get("priceRange"), minPrice);
		};
	}

	default Specification<Restaurant> maxPriceRange(Integer maxPrice) {
		return (root, query, criteriaBuilder) -> {
			if (null == maxPrice ) return null;
			return criteriaBuilder.le(root.get("priceRange"), maxPrice);
		};
	}
}
