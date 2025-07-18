package co.com.linktic.services.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.com.linktic.commons.dto.JsonApiPagedResponse;
import co.com.linktic.commons.dto.JsonApiResponse;
import co.com.linktic.commons.dto.ProductDTO;
import co.com.linktic.commons.exception.domain.ProductNotFound;
import co.com.linktic.services.controller.ProductController;
import co.com.linktic.services.entity.Product;
import co.com.linktic.services.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServices implements ProductController {

	private ModelMapper mapper;
	private ProductRepository repository;

	@Override
	public JsonApiResponse<ProductDTO> findByID(Long id) {

		var product = repository.findById(id).orElseThrow(ProductNotFound::new);
		var dto = mapper.map(product, ProductDTO.class);
		return new JsonApiResponse<>(String.valueOf(dto.getId()), dto);

	}

	@Override
	public JsonApiResponse<ProductDTO> create(ProductDTO product) {

		var newProduct = mapper.map(product, Product.class);

		repository.save(newProduct);
		log.debug("Nuevo producto creado: {}", newProduct);

		var dto = mapper.map(newProduct, ProductDTO.class);
		return new JsonApiResponse<>(String.valueOf(dto.getId()), dto);

	}

	@Override
	public JsonApiPagedResponse<ProductDTO> findProductsPage(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);
		var result = repository.findAll(pageable);

		var data = result.getContent().stream().map(product -> {
			var dto = mapper.map(product, ProductDTO.class);
			return new JsonApiResponse<>(String.valueOf(dto.getId()), dto);
		}).toList();

		return new JsonApiPagedResponse<>(data, result.getTotalPages(), result.getTotalElements(), result.getSize(),
				result.getNumber());
	}



}