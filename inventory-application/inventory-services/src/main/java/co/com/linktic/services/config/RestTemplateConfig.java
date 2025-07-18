package co.com.linktic.services.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Value("${security.product.api-key}")
	private String apiKey;

	@Bean
	RestTemplate restTemplate() {
		var restTemplate = new RestTemplate();

		restTemplate.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().add("x-api-key", apiKey);
			return execution.execute(request, body);
		});

		return restTemplate;
	}

}
