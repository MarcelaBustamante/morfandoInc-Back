package com.morfando.restaurantservice.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtConfig(@Value("${jwt.keystore.location}") String keyStorePath,
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
