package com.morfando.restaurantservice.users.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

@Getter
public class AuthenticationResult {
	private final String subject;
	private final String expiration;
	private final String token;

	public AuthenticationResult(Jwt jwt) {
		Instant expiresAt = jwt.getExpiresAt();
		assert null != expiresAt;
		this.expiration = expiresAt.toString();
		this.subject = jwt.getSubject();
		this.token = jwt.getTokenValue();
	}
}
