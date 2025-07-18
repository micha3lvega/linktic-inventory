package co.com.linktic.services.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.linktic.commons.dto.ProductDTO;

@RestController
@RequestMapping("/api/v1/product")
public interface ProductController {

	@GetMapping
	Page<ProductDTO> findProductsPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size);

	@GetMapping("/{id}")
	ProductDTO findByID(Long id);

	@PostMapping
	ProductDTO create(@RequestBody ProductDTO product);


}
