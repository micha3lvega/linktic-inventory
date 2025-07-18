package co.com.linktic.services.handler;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.com.linktic.commons.domain.exception.InsufficientStockException;
import co.com.linktic.commons.domain.exception.JsonApiError;
import co.com.linktic.commons.exception.domain.ProductNotFound;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFound.class)
	public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFound ex) {
		log.warn("Producto no encontrado: {}", ex.getMessage());

		var error = new JsonApiError("404", "Producto no encontrado",
				"El producto especificado no existe en el sistema");

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("errors", List.of(error)));
	}

	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<Map<String, Object>> handleInsufficientStock(InsufficientStockException ex) {
		log.warn("Stock insuficiente: {}", ex.getMessage());

		var error = new JsonApiError("400", "Stock insuficiente", ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", List.of(error)));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
		var errors = ex.getBindingResult().getFieldErrors().stream().map(
				err -> new JsonApiError("400", "Validación fallida", err.getField() + ": " + err.getDefaultMessage()))
				.toList();

		return ResponseEntity.badRequest().body(Map.of("errors", errors));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		var errors = ex.getConstraintViolations().stream().map(err -> new JsonApiError("400",
				"Violación de restricción", err.getPropertyPath() + ": " + err.getMessage())).toList();

		return ResponseEntity.badRequest().body(Map.of("errors", errors));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
		log.warn("Parámetro inválido: {}", ex.getMessage());

		var error = new JsonApiError("400", "Solicitud inválida", ex.getMessage());

		return ResponseEntity.badRequest().body(Map.of("errors", List.of(error)));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
		log.error("Error inesperado: {}", ex.getMessage(), ex);

		var error = new JsonApiError("500", "Error interno del servidor",
				"Ocurrió un error inesperado. Inténtalo más tarde.");

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("errors", List.of(error)));
	}
}
