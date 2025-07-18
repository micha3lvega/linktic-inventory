package co.com.linktic.services.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
	public ProductDTO findByID(Long id) {

		var product = repository.findById(id).orElseThrow(ProductNotFound::new);
		return mapper.map(product, ProductDTO.class);

	}

	@Override
	public ProductDTO create(ProductDTO product) {

		var newProduct = mapper.map(product, Product.class);

		repository.save(newProduct);
		log.debug("Nuevo producto creado: {}", newProduct);

		return mapper.map(newProduct, ProductDTO.class);
	}

	@Override
	public Page<ProductDTO> findProductsPage(int page, int size) {
		var pageable = PageRequest.of(page, size);
		return repository.findAll(pageable).map(product -> mapper.map(product, ProductDTO.class));
	}

}