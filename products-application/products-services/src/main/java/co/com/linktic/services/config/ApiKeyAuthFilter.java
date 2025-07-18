package co.com.linktic.services.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

	@Value("${security.api-key}")
	private String validApiKey;

	private static final List<String> EXCLUDED_PATHS = List.of("/swagger-ui", "/v3/api-docs", "/swagger-resources",
			"/webjars", "/h2-console", "/actuator");

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		var path = request.getRequestURI();
		return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var header = request.getHeader("x-api-key");

		if (header == null || !header.equals(validApiKey)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("API key invalida");
			return;
		}

		// Autenticar manualmente al usuario autenticado
		var authentication = new UsernamePasswordAuthenticationToken("apikey-user", null, Collections.emptyList());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
