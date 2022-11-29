package com.morfando.restaurantservice.users.application;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.morfando.restaurantservice.users.model.UserRepository;
import com.morfando.restaurantservice.users.model.entity.User;
import com.morfando.restaurantservice.users.model.entity.UserType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class Authenticate {
	private final UserRepository repo;
	private final PasswordEncoder passwordEncoder;
	private final JwtEncoder jwtEncoder;
	private final long expiration;
	private final GoogleIdTokenVerifier googleVerifier;

	public Authenticate(UserRepository repo, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder,
						JwtDecoder jwtDecoder, @Value("${jwt.expiration:3600}")  long expiration,
						@Value("${google-oauth.client-id}") String clientId)
			throws GeneralSecurityException, IOException {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
		this.jwtEncoder = jwtEncoder;
		this.expiration = expiration;
		this.googleVerifier = new GoogleIdTokenVerifier
				.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory())
				.setAudience(Collections.singletonList(clientId))
				.build();
	}

	public Jwt authenticate(User user, String providedPassword) {
		if (!passwordEncoder.matches(providedPassword, user.getPassword())) {
			throw new IllegalStateException("Invalid credentials");
		}
		return buildJwt(user);
	}

	/**
	 * @param googleTokenId
	 * @return Pair of jwt and boolean indicating if user was created
	 */
	public Pair<Jwt, Boolean> googleAuthentication(String googleTokenId) {
		try {
			GoogleIdToken token = googleVerifier.verify(googleTokenId);
			GoogleIdToken.Payload payload = token.getPayload();
			Optional<User> optional = repo.findByEmailIgnoreCase(payload.getEmail());
			User user = null;
			if (optional.isEmpty()) {
				user = new User((String) payload.get("name"), null, payload.getEmail(), null,
						(String) payload.get("picture"), UserType.CLIENT);
				user = repo.save(user);
			}
			boolean isNewUser = optional.isEmpty();
			return Pair.of(buildJwt(user), isNewUser);
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
