package co.com.linktic.services.handler;


import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.com.linktic.commons.exception.domain.ProductNotFound;

/**
 * Manejador global de excepciones para exponer errores en formato compatible con JSON:API.
 *
 * Captura excepciones específicas y las convierte en respuestas estructuradas con un arreglo de "errors".
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Maneja el caso en que no se encuentra un producto en la base de datos.
	 *
	 * @param ex la excepción lanzada cuando un producto no existe
	 * @return respuesta HTTP 404 con estructura JSON:API en el campo "errors"
	 */
	@ExceptionHandler(ProductNotFound.class)
	public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFound ex) {
		Map<String, Object> error = Map.of("errors", List.of(Map.of("status", "404", "title", "Producto no encontrado",
				"detail", ex.getMessage() != null ? ex.getMessage() : "El producto con el ID especificado no existe")));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

}
