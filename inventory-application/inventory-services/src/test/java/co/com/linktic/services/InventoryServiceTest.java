package co.com.linktic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import co.com.linktic.commons.domain.exception.InsufficientStockException;
import co.com.linktic.commons.dto.InventoryDTO;
import co.com.linktic.commons.dto.ProductDTO;
import co.com.linktic.commons.dto.PurchaseRequest;
import co.com.linktic.services.entity.Inventory;
import co.com.linktic.services.repository.InventoryRepository;
import co.com.linktic.services.rest.client.ProductRestClient;
import co.com.linktic.services.rest.client.StockMovementRepository;
import co.com.linktic.services.services.InventoryService;

class InventoryServiceTest {

	@Mock
	private ProductRestClient productRestClient;

	@Mock
	private InventoryRepository inventoryRepository;

	@Mock
	private StockMovementRepository stockMovementRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private InventoryService inventoryService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testPurchaseSuccess() {
		Long productId = 1L;
		var quantity = 5;
		var inventory = new Inventory(productId, 10, LocalDateTime.now());
		var request = new PurchaseRequest(productId, quantity);

		when(productRestClient.findById(productId)).thenReturn(new ProductDTO());
		when(inventoryRepository.findById(productId)).thenReturn(Optional.of(inventory));
		when(inventoryRepository.save(any())).thenReturn(inventory);
		when(modelMapper.map(any(), eq(InventoryDTO.class)))
		.thenReturn(new InventoryDTO(productId, 5, inventory.getUpdatedAt()));

		var response = inventoryService.purchase(request);

		assertEquals("inventory", response.getData().getType());
		assertEquals("1", response.getData().getId());
		assertEquals(5, response.getData().getAttributes().getAvailableStock());
	}

	@Test
	void testPurchaseInsufficientStock() {
		Long productId = 2L;
		var request = new PurchaseRequest(productId, 20);
		var inventory = new Inventory(productId, 10, LocalDateTime.now());

		when(productRestClient.findById(productId)).thenReturn(new ProductDTO());
		when(inventoryRepository.findById(productId)).thenReturn(Optional.of(inventory));

		assertThrows(InsufficientStockException.class, () -> inventoryService.purchase(request));
	}

	@Test
	void testUpdateStockSuccess() {
		Long productId = 3L;
		var quantity = 50;
		var inventory = new Inventory(productId, 10, LocalDateTime.now());

		when(productRestClient.findById(productId)).thenReturn(new ProductDTO());
		when(inventoryRepository.findById(productId)).thenReturn(Optional.of(inventory));
		when(modelMapper.map(any(), eq(InventoryDTO.class)))
		.thenReturn(new InventoryDTO(productId, quantity, LocalDateTime.now()));

		var response = inventoryService.updateStock(productId, quantity);

		assertEquals("inventory", response.getData().getType());
		assertEquals("3", response.getData().getId());
		assertEquals(quantity, response.getData().getAttributes().getAvailableStock());
	}

	@Test
	void testUpdateStockNewInventory() {
		Long productId = 4L;
		var quantity = 15;

		when(productRestClient.findById(productId)).thenReturn(new ProductDTO());
		when(inventoryRepository.findById(productId)).thenReturn(Optional.empty());
		when(modelMapper.map(any(), eq(InventoryDTO.class)))
		.thenReturn(new InventoryDTO(productId, quantity, LocalDateTime.now()));

		var response = inventoryService.updateStock(productId, quantity);

		assertEquals(quantity, response.getData().getAttributes().getAvailableStock());
	}
}