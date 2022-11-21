package com.morfando.restaurantservice.restaurants.model;

import com.morfando.restaurantservice.restaurants.model.entity.MenuItem;
import com.morfando.restaurantservice.restaurants.model.entity.Restaurant;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Expression;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long>, JpaSpecificationExecutor<MenuItem> {

	MenuItem findByIdAndRestaurantId(long menuItemId, long restaurantId);

	void deleteByIdAndRestaurantId(long menuItemId, long restaurantId);

	default Specification<MenuItem> restaurantId(long id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("restaurantId"), id);
	}
	default Specification<MenuItem> categoryLike(String category) {
		return (root, query, criteriaBuilder) -> {
			if (ObjectUtils.isEmpty(category)) return null;
			Expression<String> field = criteriaBuilder.lower(root.get("category"));
			return criteriaBuilder.equal(field, category.toLowerCase());
		};
	}

	default Specification<MenuItem> vegan(Boolean vegan) {
		return (root, query, criteriaBuilder) -> {
			if (null == vegan) return null;
			return criteriaBuilder.equal(root.get("vegan"), vegan);
		};
	}

	default Specification<MenuItem> tacc(Boolean tacc) {
		return (root, query, criteriaBuilder) -> {
			if (null == tacc) return null;
			return criteriaBuilder.equal(root.get("tacc"), tacc);
		};
	}
}
