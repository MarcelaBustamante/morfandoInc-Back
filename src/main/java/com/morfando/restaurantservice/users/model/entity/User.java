package com.morfando.restaurantservice.users.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
	@Schema(example = "12345")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
	private Long id;

	@Schema(example = "John")
	private String name;

	@Schema(example = "Doe")
	private String lastName;

	@Schema(example = "user@mail.com")
	@Column(unique = true)
	private String email;

	@JsonIgnore
	@Schema(example = "my-password")
	private String password;

	@Schema(example = "https://some-site/image.png")
	private String profilePicture;

	@Enumerated(EnumType.STRING)
	private UserType type;

	public User(String name, String lastName, String email, String password, String profilePicture, UserType type) {
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.profilePicture = profilePicture;
		this.type = type;
	}

	public static User publicUser(User user) {
		return new User(user.getName(), user.getLastName(), null, null, user.getProfilePicture(),
				user.getType());
	}
}
