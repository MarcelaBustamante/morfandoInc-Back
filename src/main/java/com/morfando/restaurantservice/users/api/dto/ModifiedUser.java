package com.morfando.restaurantservice.users.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifiedUser {
    @Schema(example = "John")
    private String name;

    @Schema(example = "Doe")
    private String lastName;

    @Schema(example = "user@mail.com")
    private String email;

    @Schema(example = "my-password")
    private String password;

    @Schema(example = "https://some-site/image.png")
    private String profilePicture;
}
