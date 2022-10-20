package com.morfando.restaurantservice.users.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {

	@Schema(example = "John", required = true)
	private String name;

	@Schema(example = "Doe", required = true)
	private String lastName;

	@Schema(example = "user@mail.com", required = true)
	private String email;

	@Schema(example = "my-password", required = true)
	private String password;

	@Schema(example = "https://some-site/image.png")
	private String profilePicture;
}
