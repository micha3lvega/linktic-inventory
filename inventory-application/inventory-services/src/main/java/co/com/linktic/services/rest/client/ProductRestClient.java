package co.com.linktic.services.rest.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.com.linktic.commons.dto.JsonApiDocument;
import co.com.linktic.commons.dto.ProductDTO;
import co.com.linktic.commons.exception.domain.ProductNotFound;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductRestClient {

	@Value("${products.base-url}")
	private String baseUrl;

	private RestTemplate restTemplate;

	public ProductRestClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public ProductDTO findById(Long id) {

		log.info("Buscando el producto {}", id);

		var url = baseUrl + id;
		log.info("url: {}", url);

		try {

			ResponseEntity<JsonApiDocument<ProductDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<JsonApiDocument<ProductDTO>>() {
					});

			var body = response.getBody();

			if (response.getStatusCode().is2xxSuccessful() && body != null && body.getData() != null) {
				return body.getData().getAttributes();
			}
			throw new ProductNotFound();

		} catch (Exception e) {
			log.error("Error al consultar producto {}: {}", id, e.getMessage());
			throw new ProductNotFound();
		}
	}

}
