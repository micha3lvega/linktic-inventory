package co.com.linktic.services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import co.com.linktic.services.filter.ApiKeyAuthFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final ApiKeyAuthFilter apiKeyAuthFilter;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
								"/webjars/**", "/h2-console/**").permitAll().anyRequest().authenticated())
				.csrf(CsrfConfigurer::disable).headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
				.addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
}