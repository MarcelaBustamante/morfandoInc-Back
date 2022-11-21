package com.morfando.restaurantservice.users.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {

	@NotBlank
	@Schema(example = "John", required = true)
	private String name;

	@NotBlank
	@Schema(example = "Doe", required = true)
	private String lastName;

	@NotBlank
	@Schema(example = "user@mail.com", required = true)
	private String email;

	@NotBlank
	@Schema(example = "my-password", required = true)
	private String password;

	@Schema(example = "https://some-site/image.png")
	private String profilePicture;
}
