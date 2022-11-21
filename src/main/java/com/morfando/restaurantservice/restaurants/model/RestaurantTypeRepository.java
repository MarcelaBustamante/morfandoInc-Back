package com.morfando.restaurantservice.restaurants.model;

import com.morfando.restaurantservice.restaurants.model.entity.RestaurantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTypeRepository extends JpaRepository<RestaurantType, Long> {
}
