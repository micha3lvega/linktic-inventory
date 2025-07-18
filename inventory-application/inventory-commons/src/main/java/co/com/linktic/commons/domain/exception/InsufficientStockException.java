package co.com.linktic.commons.domain.exception;

public class InsufficientStockException extends RuntimeException {

	private static final long serialVersionUID = -8803097577796424210L;

	public InsufficientStockException(Long productId, int requested, int available) {
		super("Inventario insuficiente para el producto " + productId + ": solicitado " + requested + ", disponible "
				+ available);
	}

}
