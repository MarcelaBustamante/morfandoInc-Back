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
		value = "SELECT *, " +
				" (6371 * ACOS( COS(RADIANS(?5)) * COS(RADIANS(LATITUDE)) * COS(RADIANS(LONGITUDE) - RADIANS(?6)) " +
				"+ SIN(RADIANS(?5)) * SIN(RADIANS(LATITUDE)) )) AS DISTANCE " +
				"FROM RESTAURANT WHERE (?1 IS NULL OR TYPE = ?1) " +
				"AND ACTIVE = true " +
				"AND DELETED = false " +
				"AND (?2 IS NULL OR PRICE_RANGE >= ?2) " +
				"AND (?3 IS NULL OR PRICE_RANGE <= ?3) " +
				"AND (?4 IS NULL OR RATING >= ?4) " +
				"AND (?7 IS NULL OR LOWER(NAME) LIKE ?7) " +
				"AND (6371 * ACOS( COS(RADIANS(?5)) * COS(RADIANS(LATITUDE)) * COS(RADIANS(LONGITUDE) - RADIANS(?6)) " +
				"+ SIN(RADIANS(?5)) * SIN(RADIANS(LATITUDE)) )) <= ?8 "
	)
	Page<Restaurant> findAllWithFilters(String type, Integer minPrice, Integer maxPrice, Integer minRating, double lat,
										double lng, String search, int radius, Pageable pageable);

	List<Restaurant> findByOwnerAndDeletedFalse(long owner);
}
