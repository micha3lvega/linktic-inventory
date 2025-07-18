package co.com.linktic.commons.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {

	private Long productId;
	private int availableStock;
	private LocalDateTime updatedAt;

}
