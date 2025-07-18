package co.com.linktic.services.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.linktic.commons.dto.ProductDTO;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testCreateProduct() throws Exception {
		var dto = new ProductDTO(null, "Reloj", 199.99, "Digital");

		var body = objectMapper.writeValueAsString(dto);

		var result = mockMvc.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isOk()).andExpect(jsonPath("$.data.type").value("product"))
				.andExpect(jsonPath("$.data.attributes.nombre").value("Reloj")).andReturn();

		var response = result.getResponse().getContentAsString();
		var json = objectMapper.readTree(response);
		assertNotNull(json.get("data").get("id"));
	}

	@Test
	void testFindById() throws Exception {
		var dto = new ProductDTO(null, "Mouse", 29.99, "Inalámbrico");

		var requestBody = objectMapper.writeValueAsString(dto);
		var createResult = mockMvc
				.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isOk()).andReturn();

		var json = objectMapper.readTree(createResult.getResponse().getContentAsString());
		var id = json.get("data").get("id").asText();

		mockMvc.perform(get("/api/v1/product/" + id)).andExpect(status().isOk())
		.andExpect(jsonPath("$.data.id").value(id))
		.andExpect(jsonPath("$.data.attributes.nombre").value("Mouse"));
	}

	@Test
	void testFindById_NotFound() throws Exception {
		mockMvc.perform(get("/api/v1/product/99999")).andExpect(status().isNotFound())
		.andExpect(jsonPath("$.errors[0].title").value("Producto no encontrado"));
	}

	@Test
	void testPagination() throws Exception {
		for (var i = 0; i < 5; i++) {
			var dto = new ProductDTO(null, "Producto " + i, 10.0 + i, "Descripción");
			mockMvc.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
		}

		mockMvc.perform(get("/api/v1/product?page=0&size=3")).andExpect(status().isOk())
		.andExpect(jsonPath("$.data.length()").value(3)).andExpect(jsonPath("$.meta.totalElements").exists());
	}
}
