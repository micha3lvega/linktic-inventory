package co.com.linktic.commons.dto;

import lombok.Data;

@Data
public class JsonApiDocument<T> {
	private final JsonApiResponse<T> data;

	public JsonApiDocument(JsonApiResponse<T> data) {
		this.data = data;
	}

	public JsonApiResponse<T> getData() {
		return data;
	}
}
