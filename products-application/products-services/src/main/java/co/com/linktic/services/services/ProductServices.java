package co.com.linktic.services.services;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.com.linktic.commons.dto.JsonApiDocument;
import co.com.linktic.commons.dto.JsonApiPagedResponse;
import co.com.linktic.commons.dto.JsonApiResponse;
import co.com.linktic.commons.dto.ProductDTO;
import co.com.linktic.commons.exception.domain.ProductNotFound;
import co.com.linktic.services.controller.ProductController;
import co.com.linktic.services.entity.Product;
import co.com.linktic.services.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado de manejar la lógica de negocio relacionada con productos.
 *
 * Implementa la interfaz {@link ProductController} y devuelve las respuestas en formato JSON:API.
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProductServices implements ProductController {


	private final ModelMapper mapper;
	private final ProductRepository repository;

	/**
	 * Obtiene un producto por su ID.
	 *
	 * @param id Identificador del producto
	 * @return Documento JSON:API con el producto encontrado
	 * @throws ProductNotFound si el producto no existe en la base de datos
	 */
	@Override
	public JsonApiDocument<ProductDTO> findByID(Long id) {
		var product = repository.findById(id).orElseThrow(ProductNotFound::new);
		var dto = mapper.map(product, ProductDTO.class);
		var response = new JsonApiResponse<>(String.valueOf(dto.getId()), dto);
		return new JsonApiDocument<>(response);
	}

	/**
	 * Crea un nuevo producto en el sistema.
	 *
	 * @param product DTO con los datos del nuevo producto
	 * @return Documento JSON:API con el producto creado
	 */
	@Override
	public JsonApiDocument<ProductDTO> create(ProductDTO product) {
		var newProduct = mapper.map(product, Product.class);
		repository.save(newProduct);

		log.debug("Nuevo producto creado: {}", newProduct);

		var dto = mapper.map(newProduct, ProductDTO.class);
		var response = new JsonApiResponse<>(String.valueOf(dto.getId()), dto);
		return new JsonApiDocument<>(response);
	}

	/**
	 * Obtiene una lista paginada de productos.
	 *
	 * @param page Número de página (empezando en 0)
	 * @param size Cantidad de elementos por página
	 * @return Respuesta JSON:API con los productos y metainformación de paginación
	 */
	@Override
	public JsonApiPagedResponse<ProductDTO> findProductsPage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		var result = repository.findAll(pageable);

		var data = result.getContent().stream().map(product -> {
			var dto = mapper.map(product, ProductDTO.class);
			return new JsonApiResponse<>(String.valueOf(dto.getId()), dto);
		}).collect(Collectors.toList());

		return new JsonApiPagedResponse<>(data, result.getTotalPages(), result.getTotalElements(), result.getSize(),
				result.getNumber());
	}
}
