package co.com.linktic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import co.com.linktic.commons.dto.ProductDTO;
import co.com.linktic.commons.exception.domain.ProductNotFound;
import co.com.linktic.services.entity.Product;
import co.com.linktic.services.repository.ProductRepository;
import co.com.linktic.services.services.ProductServices;

@ExtendWith(MockitoExtension.class)
class ProductServicesTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ModelMapper mapper;

	@InjectMocks
	private ProductServices productServices;

	@Test
	void testCreateProduct() {
		var inputDto = new ProductDTO(null, "Camisa", 49.0, "Algodón");
		var savedProduct = new Product(1L, "Camisa", 49.0, "Algodón");
		var outputDto = new ProductDTO(1L, "Camisa", 49.0, "Algodón");

		when(mapper.map(inputDto, Product.class)).thenReturn(savedProduct);
		when(productRepository.save(savedProduct)).thenReturn(savedProduct);
		when(mapper.map(savedProduct, ProductDTO.class)).thenReturn(outputDto);

		var result = productServices.create(inputDto);
		var response = result.getData();

		assertNotNull(response);
		assertEquals("product", response.getType());
		assertEquals("1", response.getId());
		assertEquals("Camisa", response.getAttributes().getNombre());
		assertEquals(49.0, response.getAttributes().getPrecio());
	}

	@Test
	void testFindByIdSuccess() {
		var product = new Product(2L, "Zapato", 89.0, "Cuero");
		var dto = new ProductDTO(2L, "Zapato", 89.0, "Cuero");

		when(productRepository.findById(2L)).thenReturn(Optional.of(product));
		when(mapper.map(product, ProductDTO.class)).thenReturn(dto);

		var result = productServices.findByID(2L);
		var response = result.getData();

		assertNotNull(response);
		assertEquals("2", response.getId());
		assertEquals("Zapato", response.getAttributes().getNombre());
	}

	@Test
	void testFindByIdNotFound() {
		when(productRepository.findById(99L)).thenReturn(Optional.empty());
		assertThrows(ProductNotFound.class, () -> productServices.findByID(99L));
	}

	@Test
	void testFindProductsPage() {
		List<Product> products = List.of(new Product(1L, "Camisa", 49.0, "Algodón"),
				new Product(2L, "Zapato", 89.0, "Cuero"));

		Page<Product> page = new PageImpl<>(products, PageRequest.of(0, 10), 2);

		when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
		when(mapper.map(products.get(0), ProductDTO.class)).thenReturn(new ProductDTO(1L, "Camisa", 49.0, "Algodón"));
		when(mapper.map(products.get(1), ProductDTO.class)).thenReturn(new ProductDTO(2L, "Zapato", 89.0, "Cuero"));

		var result = productServices.findProductsPage(0, 10);

		assertEquals(2, result.getData().size());
		assertEquals(1, result.getMeta().get("totalPages"));
		assertEquals(2L, result.getMeta().get("totalElements"));
	}
}
