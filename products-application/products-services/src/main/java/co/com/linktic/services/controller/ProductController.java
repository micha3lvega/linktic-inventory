package co.com.linktic.services.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.linktic.commons.dto.JsonApiPagedResponse;
import co.com.linktic.commons.dto.JsonApiResponse;
import co.com.linktic.commons.dto.ProductDTO;

@RestController
@RequestMapping("/api/v1/product")
public interface ProductController {

	@GetMapping
	JsonApiPagedResponse<ProductDTO> findProductsPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size);

	@GetMapping("/{id}")
	JsonApiResponse<ProductDTO> findByID(@PathVariable Long id);

	@PostMapping
	JsonApiResponse<ProductDTO> create(@RequestBody ProductDTO product);


}
