package co.com.linktic.commons.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest {

	private Long productId;

	@Positive(message = "La cantidad debe ser mayor que cero")
	private int quantity;

}
