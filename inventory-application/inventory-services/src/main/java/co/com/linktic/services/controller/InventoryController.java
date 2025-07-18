package co.com.linktic.services.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.linktic.commons.dto.InventoryDTO;
import co.com.linktic.commons.dto.JsonApiDocument;
import co.com.linktic.commons.dto.PurchaseRequest;
import co.com.linktic.services.services.InventoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {

	private final InventoryService inventoryService;

	/**
	 * Registra una compra y actualiza el stock sumando la cantidad comprada.
	 *
	 * @param request objeto con productId y quantity
	 */
	@PostMapping("/purchase")
	public ResponseEntity<JsonApiDocument<InventoryDTO>> purchase(@RequestBody @Valid PurchaseRequest request) {
		return ResponseEntity.ok(inventoryService.purchase(request));
	}

	@PutMapping("/{productId}/stock")
	public ResponseEntity<JsonApiDocument<InventoryDTO>> updateStock(@PathVariable Long productId,
			@RequestParam int quantity) {
		return ResponseEntity.ok(inventoryService.updateStock(productId, quantity));
	}


}
