package com.morfando.restaurantservice.restaurants.model;

import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {
	@Query(
		nativeQuery = true,
		value = "SELECT *, ( (ABS(LATITUDE - ?5) + ABS(LONGITUDE - ?6)) / 2 ) AS DIST " +
				"FROM RESTAURANT WHERE (?1 IS NULL OR TYPE = ?1) " +
				"AND (?2 IS NULL OR PRICE_RANGE >= ?2) " +
				"AND (?3 IS NULL OR PRICE_RANGE <= ?3) " +
				"AND (?4 IS NULL OR RATING >= ?4) " +
				"ORDER BY ?#{#pageable}"
	)
	Page<Restaurant> findAllWithFilters(String type, Integer minPrice, Integer maxPrice, Integer minRating, Double lat,
										Double lng, Pageable pageable);
	List<Restaurant> findByOwner(long owner);
}
