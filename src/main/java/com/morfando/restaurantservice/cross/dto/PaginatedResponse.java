package com.morfando.restaurantservice.cross.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
	private long itemCount;
	private int pageCount;
	private Collection<T> items;

	public static <T> PaginatedResponse<T> from(Page<T> page) {
		return new PaginatedResponse<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
	}
}
