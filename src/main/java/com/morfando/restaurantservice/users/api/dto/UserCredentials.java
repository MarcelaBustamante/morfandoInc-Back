package com.morfando.restaurantservice.users.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserCredentials {

	@NotBlank
	@Schema(example = "user@mail.com", required = true)
	private String email;

	@NotBlank
	@Schema(example = "my-password", required = true)
	private String password;
}
