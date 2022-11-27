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
	private final boolean firstLogin;

	public AuthenticationResult(Jwt jwt, boolean firstLogin) {
		Instant expiresAt = jwt.getExpiresAt();
		this.expiration = null != expiresAt ? expiresAt.toString() : null;
		this.subject = jwt.getSubject();
		this.token = jwt.getTokenValue();
		this.firstLogin = firstLogin;
	}
}
