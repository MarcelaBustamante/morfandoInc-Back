package com.morfando.restaurantservice.config;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ScopeBuilder;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OauthConfig {

	@Bean
	public OAuth20Service oAuthService(@Value("${google-oauth.client-id}") String clientId,
									   @Value("${google-oauth.client-secret}") String clientSecret,
									   @Value("${google-oauth.callback}") String callbackURL) {
		String[] scopes = {
			"https://www.googleapis.com/auth/userinfo.email",
			"https://www.googleapis.com/auth/userinfo.profile"
		};
		return new ServiceBuilder(clientId)
				.debug()
				.apiSecret(clientSecret)
				.defaultScope(new ScopeBuilder().withScopes(scopes).build())
				.callback(callbackURL)
				.build(GoogleApi20.instance());
	}
}
