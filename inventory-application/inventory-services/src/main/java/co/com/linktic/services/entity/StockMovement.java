package co.com.linktic.services.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import co.com.linktic.commons.domain.data.MovementType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stock_movements")
public class StockMovement implements Serializable {

	private static final long serialVersionUID = -8155227866100054012L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long productId;

	private int quantityChange;

	@Enumerated(EnumType.STRING)
	private MovementType movementType;

	private LocalDateTime timestamp;

	@PrePersist
	public void prePersist() {
		this.timestamp = LocalDateTime.now();
	}
}
