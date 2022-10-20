package com.morfando.restaurantservice.users.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserCredentials {

	@Schema(example = "user@mail.com", required = true)
	private String email;

	@Schema(example = "my-password", required = true)
	private String password;
}
