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

/**
 * Servicio de dominio para la gestión de productos.
 *
 * Esta clase implementa las operaciones declaradas en {@link ProductController} y maneja las funciones principales relacionadas con el modelo {@link Product}, como:
 *
 * - Crear un nuevo producto - Buscar producto por ID - Listar productos paginados
 *
 * Todas las respuestas están formateadas según el estándar JSON:API, utilizando {@link JsonApiResponse} y {@link JsonApiPagedResponse}.
 *
 * Utiliza un {@link ProductRepository} para acceder a la capa de persistencia, y {@link ModelMapper} para convertir entre entidades y DTOs.
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProductServices implements ProductController {

	private ModelMapper mapper;
	private ProductRepository repository;

	/**
	 * Obtiene un producto por su ID.
	 *
	 * @param id Identificador del producto
	 * @return Respuesta JSON:API con el producto encontrado
	 * @throws ProductNotFound si el producto no existe
	 */
	@Override
	public JsonApiResponse<ProductDTO> findByID(Long id) {

		var product = repository.findById(id).orElseThrow(ProductNotFound::new);
		var dto = mapper.map(product, ProductDTO.class);
		return new JsonApiResponse<>(String.valueOf(dto.getId()), dto);

	}

	/**
	 * Crea un nuevo producto en el sistema.
	 *
	 * @param product DTO con los datos del nuevo producto
	 * @return Respuesta JSON:API con el producto creado
	 */
	@Override
	public JsonApiResponse<ProductDTO> create(ProductDTO product) {

		var newProduct = mapper.map(product, Product.class);

		repository.save(newProduct);
		log.debug("Nuevo producto creado: {}", newProduct);

		var dto = mapper.map(newProduct, ProductDTO.class);
		return new JsonApiResponse<>(String.valueOf(dto.getId()), dto);

	}

	/**
	 * Obtiene una lista paginada de productos.
	 *
	 * @param page Número de página (empezando en 0)
	 * @param size Cantidad de elementos por página
	 * @return Respuesta JSON:API con la lista de productos y metadatos de paginación
	 */
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