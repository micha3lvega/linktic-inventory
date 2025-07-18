package co.com.linktic.commons.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductDTO implements Serializable {

	private static final long serialVersionUID = -2758507414819619408L;

	private String id;

	@NotBlank(message = "El nombre del producto es obligatorio")
	@Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
	private String nombre;

	@NotNull(message = "El precio es obligatorio")
	@Positive(message = "El precio debe ser mayor a cero")
	private Double precio;

	@Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
	private String descripcion;

}
