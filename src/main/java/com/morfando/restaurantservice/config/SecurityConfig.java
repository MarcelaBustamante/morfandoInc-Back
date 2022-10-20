package com.morfando.restaurantservice.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final RSAPrivateKey privateKey;
	private final RSAPublicKey publicKey;

	public SecurityConfig(@Value("${jwt.keystore.location}") String keyStorePath,
						  @Value("${jwt.keystore.password}") String keyStorePassword,
						  @Value("${jwt.keystore.key.alias}") String keyAlias,
						  @Value("${jwt.keystore.key.password}") String keyPassword) {
		try (
				InputStream resourceAsStream = Thread
						.currentThread().getContextClassLoader().getResourceAsStream(keyStorePath)
		) {
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(resourceAsStream, keyStorePassword.toCharArray());
			this.publicKey = (RSAPublicKey) ks.getCertificate(keyAlias).getPublicKey();
			this.privateKey = (RSAPrivateKey) ks.getKey(keyAlias, keyPassword.toCharArray());
		} catch (Exception e) {
			throw new IllegalStateException("Error setting up jwt keys", e);
		}
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 return http.authorizeHttpRequests(registry -> registry
				.antMatchers(HttpMethod.POST,"/users/login").permitAll()
			 	.antMatchers(HttpMethod.GET,"/users/oauth").permitAll()
				.antMatchers(HttpMethod.POST, "/users").permitAll()
				.antMatchers("/h2/**").permitAll()
			 	.antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.anyRequest().authenticated()
			)
			.csrf().disable()
			.cors()
			.and().headers().frameOptions().disable()
			.and().oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	 		.build();
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(publicKey).build();
	}

	@Bean
	JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}
}
