package co.com.linktic.services.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import co.com.linktic.services.entity.Inventory;
import co.com.linktic.services.repository.InventoryRepository;
import co.com.linktic.services.rest.client.ProductRestClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class InventoryService {

	private final InventoryRepository inventoryRepository;
	private final ProductRestClient productoRestClient;

	public void updateStock(Long productId, int newQuantity) {

		log.info("Inicia actualizacion el stock del producto {} a {} unidades", productId, newQuantity);

		// Verifica que el producto exista
		productoRestClient.findById(productId);

		// Validar que el stock no sea negativo
		if (newQuantity < 0) {
			log.warn("Stock negativo no permitido: producto {} con cantidad {}", productId, newQuantity);
			throw new IllegalArgumentException("La cantidad disponible no puede ser negativa");
		}

		// Buscar inventario local o crear uno nuevo
		var inventory = inventoryRepository.findById(productId).orElse(new Inventory(productId, 0, null));

		// Actualizar el stock
		inventory.setAvailableStock(newQuantity);
		inventory.setUpdatedAt(LocalDateTime.now());

		inventoryRepository.save(inventory);

		log.info("Inventario actualizado correctamente para producto {}: {} unidades, inventario: {}", productId,
				newQuantity, inventory);

	}
}
