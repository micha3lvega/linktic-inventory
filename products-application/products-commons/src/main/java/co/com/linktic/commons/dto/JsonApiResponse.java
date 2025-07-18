package co.com.linktic.commons.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class JsonApiResponse<T> implements Serializable {

	private static final long serialVersionUID = 7182246982151547214L;

	private final String type;
	private final String id;
	private final T attributes;

	public JsonApiResponse(String id, T attributes) {
		this.type = "product";
		this.id = id;
		this.attributes = attributes;
	}

}
