package com.morfando.restaurantservice.users.api.dto;

import lombok.Getter;

@Getter
public class GoogleUserProfile {
	private String id;
	private String email;
	private String name;
	private String picture;
}
