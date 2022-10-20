package com.morfando.restaurantservice.users.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth.OAuthService;
import com.morfando.restaurantservice.users.api.dto.AuthenticationResult;
import com.morfando.restaurantservice.users.api.dto.GoogleUserProfile;
import com.morfando.restaurantservice.users.api.dto.UserCredentials;
import com.morfando.restaurantservice.users.api.dto.NewUser;
import com.morfando.restaurantservice.users.model.User;
import com.morfando.restaurantservice.users.model.UserType;
import com.morfando.restaurantservice.users.model.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {
	private final UsersRepository repo;
	private final PasswordEncoder passwordEncoder;
	private final JwtEncoder jwtEncoder;

	private final OAuth20Service oAuthService;

	private final ObjectMapper objectMapper;

	private final long expiration;

//	@PreAuthorize("hasAuthority('SCOPE_CUSTOMER')")
//	@PreAuthorize("hasAuthority('SCOPE_PARTNER')")

	public UsersController(UsersRepository repo, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder,
						   ObjectMapper objectMapper, OAuth20Service oAuthService, @Value("${jwt.expiration:3600}") long expiration) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
		this.jwtEncoder = jwtEncoder;
		this.oAuthService = oAuthService;
		this.objectMapper = objectMapper;
		this.expiration = expiration;
	}

	@PostMapping("/login")
	public AuthenticationResult login(@RequestBody UserCredentials credentials) {
		User user = repo.findByEmailIgnoreCase(credentials.getEmail())
				.orElseThrow(() -> new IllegalStateException("Invalid credentials"));
		if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
			throw new IllegalStateException("Invalid credentials");
		}
		Jwt token = buildJwt(user);
		return new AuthenticationResult(token);
	}

	@GetMapping("/oauth")
	public void oAuth(@RequestParam(value = "code", required = false) String code, HttpServletResponse response) throws IOException {
		if (null == code) {
			log.info("Redirecting to authorization URL...");
			response.sendRedirect(oAuthService.getAuthorizationUrl());
			return;
		}
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
							profile.getPicture(), UserType.CUSTOMER);
					return repo.save(nUser);
				});
			Jwt jwt = buildJwt(user);
			Cookie tokenCookie = new Cookie("auth_token", jwt.getTokenValue());
			response.addCookie(tokenCookie);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@GetMapping("/{user-id}")
	public User findUserById(@PathVariable("user-id") long id, Authentication authentication) {
		if (null == authentication || !authentication.isAuthenticated()) {
			throw new IllegalStateException();
		}
		User user = repo.findById(id).orElseThrow();
		if (!authentication.getName().equals(user.getEmail())) {
			return User.publicUser(user);
		}
		return user;
	}

	@PostMapping
	public User createUser(@RequestBody NewUser newUser) {
		Optional<User> opt = repo.findByEmailIgnoreCase(newUser.getEmail());
		if (opt.isPresent()) {
			throw new IllegalArgumentException("Email in use");
		}
		String password = passwordEncoder.encode(newUser.getPassword());
		String email = newUser.getEmail().toLowerCase();
		User user = new User(newUser.getName(), newUser.getLastName(), email, password, newUser.getProfilePicture(),
				UserType.PARTNER);
		return repo.save(user);
	}

	@DeleteMapping
	@Transactional
	public void deleteMyUser(Authentication authentication) {
		if (null == authentication || !authentication.isAuthenticated()) {
			throw new IllegalStateException();
		}
		String email = authentication.getName();
		repo.deleteByEmail(email);
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
