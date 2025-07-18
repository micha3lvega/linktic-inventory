package co.com.linktic.services.services;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import co.com.linktic.commons.domain.data.MovementType;
import co.com.linktic.commons.domain.exception.InsufficientStockException;
import co.com.linktic.commons.dto.InventoryDTO;
import co.com.linktic.commons.dto.JsonApiDocument;
import co.com.linktic.commons.dto.JsonApiResponse;
import co.com.linktic.commons.dto.PurchaseRequest;
import co.com.linktic.services.entity.Inventory;
import co.com.linktic.services.entity.StockMovement;
import co.com.linktic.services.repository.InventoryRepository;
import co.com.linktic.services.rest.client.ProductRestClient;
import co.com.linktic.services.rest.client.StockMovementRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class InventoryService {

	private final ProductRestClient productoRestClient;
	private final InventoryRepository inventoryRepository;
	private final StockMovementRepository stockMovementRepository;
	private final ModelMapper mapper;

	/**
	 * Realiza una venta: descuenta del stock y registra movimiento
	 *
	 * @param request objeto con productId y cantidad
	 * @return Inventario actualizado como JSON:API
	 */
	public JsonApiDocument<InventoryDTO> purchase(PurchaseRequest request) {
		log.info("Registrando venta: producto {} - cantidad {}", request.getProductId(), request.getQuantity());

		productoRestClient.findById(request.getProductId());

		var inventory = inventoryRepository.findById(request.getProductId())
				.orElseThrow(() -> new IllegalStateException("El inventario del producto no existe"));

		var stockActual = inventory.getAvailableStock();

		if (stockActual < request.getQuantity()) {
			log.warn("Stock insuficiente: producto {}, disponible {}, solicitado {}", request.getProductId(),
					stockActual, request.getQuantity());
			throw new InsufficientStockException(request.getProductId(), request.getQuantity(), stockActual);
		}

		var nuevoStock = stockActual - request.getQuantity();
		inventory.setAvailableStock(nuevoStock);
		inventory.setUpdatedAt(LocalDateTime.now());
		inventoryRepository.save(inventory);

		stockMovementRepository.save(StockMovement.builder().productId(request.getProductId())
				.quantityChange(-request.getQuantity()).movementType(MovementType.SALE).build());

		log.info("Venta registrada: producto {}, nuevo stock: {}", request.getProductId(), nuevoStock);

		return toJsonApi(inventory);
	}

	/**
	 * Actualiza directamente la cantidad de stock
	 *
	 * @param productId   ID del producto
	 * @param newQuantity nueva cantidad disponible
	 * @return Inventario actualizado como JSON:API
	 */
	public JsonApiDocument<InventoryDTO> updateStock(Long productId, int newQuantity) {
		productoRestClient.findById(productId);

		var inventory = inventoryRepository.findById(productId).orElse(new Inventory(productId, 0, null));

		inventory.setAvailableStock(newQuantity);
		inventory.setUpdatedAt(LocalDateTime.now());
		inventoryRepository.save(inventory);

		return toJsonApi(inventory);
	}

	/**
	 * Convierte entidad Inventory a JsonApiDocument<InventoryDTO>
	 */
	private JsonApiDocument<InventoryDTO> toJsonApi(Inventory inventory) {
		var dto = mapper.map(inventory, InventoryDTO.class);
		var response = new JsonApiResponse<>("inventory", String.valueOf(dto.getProductId()), dto);
		return new JsonApiDocument<>(response);
	}
}
