package co.com.linktic.commons.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonApiResponse<T> implements Serializable {

	private static final long serialVersionUID = 7182246982151547214L;

	private String type;
	private String id;
	private T attributes;

	public JsonApiResponse(String id, T attributes) {
		this.type = "product";
		this.id = id;
		this.attributes = attributes;
	}

}
