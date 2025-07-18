package co.com.linktic.commons.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonApiError {

	private String status;
	private String title;
	private String detail;

}
