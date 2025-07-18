package co.com.linktic.services.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "inventory")
public class Inventory implements Serializable {

	private static final long serialVersionUID = 7473785574571080933L;

	@Id
	private Long productId; // ID del producto

	private int availableStock;

	private LocalDateTime updatedAt;

	@PreUpdate
	@PrePersist
	public void updateTimestamp() {
		this.updatedAt = LocalDateTime.now();
	}

}
