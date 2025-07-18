package co.com.linktic.commons.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class JsonApiPagedResponse<T> implements Serializable {

	private static final long serialVersionUID = -519354209251487874L;

	private List<JsonApiResponse<T>> data;
	private Map<String, Object> meta;

	public JsonApiPagedResponse(List<JsonApiResponse<T>> data, int totalPages, long totalElements, int size,
			int number) {
		this.data = data;
		this.meta = Map.of("totalPages", totalPages, "totalElements", totalElements, "size", size, "number", number);
	}

	public List<JsonApiResponse<T>> getData() {
		return data;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}
}
