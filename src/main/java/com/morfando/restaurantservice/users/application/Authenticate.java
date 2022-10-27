package com.morfando.restaurantservice.users.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.morfando.restaurantservice.users.api.dto.GoogleUserProfile;
import com.morfando.restaurantservice.users.model.UserRepository;
import com.morfando.restaurantservice.users.model.entity.User;
import com.morfando.restaurantservice.users.model.entity.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class Authenticate {
	private final UserRepository repo;
	private final PasswordEncoder passwordEncoder;
	private final JwtEncoder jwtEncoder;
	private final OAuth20Service oAuthService;
	private final ObjectMapper objectMapper;
	private final long expiration;

	public Authenticate(UserRepository repo, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder,
						OAuth20Service oAuthService, ObjectMapper objectMapper,
						@Value("${jwt.expiration:3600}")  long expiration) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
		this.jwtEncoder = jwtEncoder;
		this.oAuthService = oAuthService;
		this.objectMapper = objectMapper;
		this.expiration = expiration;
	}

	public Jwt authenticate(User user, String providedPassword) {
		if (!passwordEncoder.matches(providedPassword, user.getPassword())) {
			throw new IllegalStateException("Invalid credentials");
		}
		return buildJwt(user);
	}

	public String getOAuthURL() {
		return oAuthService.getAuthorizationUrl();
	}

	public Jwt oauth(String code) {
		try {
			OAuth2AccessToken token = oAuthService.getAccessToken(code);
			OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v1/userinfo?alt=json");
			oAuthService.signRequest(token, request);
			Response oAuthResponse = oAuthService.execute(request);
			log.info("Oauth response: {}", oAuthResponse.getBody());
			GoogleUserProfile profile = objectMapper.readValue(oAuthResponse.getBody(), GoogleUserProfile.class);
			User user = repo.findByEmailIgnoreCase(profile.getEmail())
					.orElseGet(() -> {
						User nUser = new User(profile.getName(), null, profile.getEmail(), null,
								profile.getPicture(), UserType.CLIENT);
						return repo.save(nUser);
					});
			return buildJwt(user);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * @param user
	 * @return jwt for user
	 */
	private Jwt buildJwt(User user) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("self")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(expiration))
				.subject(user.getEmail())
				.claim("scope", user.getType().name())
				.build();
		return jwtEncoder.encode(JwtEncoderParameters.from(claims));
	}
}
