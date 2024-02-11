package com.steven.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steven.constant.ProductCategory;
import com.steven.dto.ProductRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void getProduct_success() throws Exception {
		RequestBuilder reuestBuilder = MockMvcRequestBuilders.get("/product/{productId}", 1);
		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.productName", equalTo("蘋果（澳洲）")))
				.andExpect(jsonPath("$.category", equalTo("FOOD"))).andExpect(jsonPath("$.imageUrl", notNullValue()))
				.andExpect(jsonPath("$.price", notNullValue())).andExpect(jsonPath("$.stock", notNullValue()))
				.andExpect(jsonPath("$.description", notNullValue()))
				.andExpect(jsonPath("$.createDate", notNullValue()))
				.andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
	}

	@Test
	public void getProduct_noFound() throws Exception {
		RequestBuilder reuestBuilder = MockMvcRequestBuilders.get("/product/{productId}", 1000);
		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(404));
	}

	@Transactional
	@Test
	public void createProduct_success() throws Exception {
		ProductRequest request = new ProductRequest();
		request.setProductName("test product");
		request.setDescription("test desc");
		request.setCategory(ProductCategory.FOOD);
		request.setImageUrl("http://test.com");
		request.setPrice(100);
		request.setStock(2);

		String json = objectMapper.writeValueAsString(request);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.post("/products").contentType(MediaType.APPLICATION_JSON)
				.content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(201))
				.andExpect(jsonPath("$.productName", equalTo(request.getProductName())))
				.andExpect(jsonPath("$.category", equalTo(request.getCategory().name())))
				.andExpect(jsonPath("$.imageUrl", equalTo(request.getImageUrl())))
				.andExpect(jsonPath("$.price", equalTo(request.getPrice())))
				.andExpect(jsonPath("$.stock", equalTo(request.getStock())))
				.andExpect(jsonPath("$.description", equalTo(request.getDescription())))
				.andExpect(jsonPath("$.createDate", notNullValue()))
				.andExpect(jsonPath("$.lastModifiedDate", notNullValue()));

	}

	@Transactional
	@Test
	public void createProduct_illegalArgument() throws Exception {
		ProductRequest request = new ProductRequest();
		request.setProductName("test product");

		String json = objectMapper.writeValueAsString(request);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.post("/products").contentType(MediaType.APPLICATION_JSON)
				.content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(400));

	}

	@Transactional
	@Test
	public void updateProduct_success() throws Exception {
		ProductRequest request = new ProductRequest();
		request.setProductName("test product");
		request.setDescription("test desc");
		request.setCategory(ProductCategory.FOOD);
		request.setImageUrl("http://test.com");
		request.setPrice(100);
		request.setStock(2);

		String json = objectMapper.writeValueAsString(request);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.put("/products/{productId}", 3)
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(200))
				.andExpect(jsonPath("$.productName", equalTo(request.getProductName())))
				.andExpect(jsonPath("$.category", equalTo(request.getCategory().name())))
				.andExpect(jsonPath("$.imageUrl", equalTo(request.getImageUrl())))
				.andExpect(jsonPath("$.price", equalTo(request.getPrice())))
				.andExpect(jsonPath("$.stock", equalTo(request.getStock())))
				.andExpect(jsonPath("$.description", equalTo(request.getDescription())))
				.andExpect(jsonPath("$.createDate", notNullValue()))
				.andExpect(jsonPath("$.lastModifiedDate", notNullValue()));

	}

	@Transactional
	@Test
	public void updateProduct_illegalArgument() throws Exception {
		ProductRequest request = new ProductRequest();
		request.setProductName("test product");

		String json = objectMapper.writeValueAsString(request);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.put("/products/{productId}", 3)
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(400));

	}

	@Transactional
	@Test
	public void updateProduct_productNotFound() throws Exception {
		ProductRequest request = new ProductRequest();
		request.setProductName("test product");
		request.setDescription("test desc");
		request.setCategory(ProductCategory.FOOD);
		request.setImageUrl("http://test.com");
		request.setPrice(100);
		request.setStock(2);

		String json = objectMapper.writeValueAsString(request);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.put("/products/{productId}", 100)
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(404));

	}

	@Transactional
	@Test
	public void delete_success() throws Exception {

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.delete("/products/{productId}", 3);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(204));
	}

	@Transactional
	@Test
	public void delete_deleteNoExistingProduct() throws Exception {

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.delete("/products/{productId}", 2000);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(204));
	}

	// 查詢商品列表
	@Test
	public void getProducts() throws Exception {

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.get("/products");

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.limit", notNullValue())).andExpect(jsonPath("$.offset", notNullValue()))
				.andExpect(jsonPath("$.total", notNullValue())).andExpect(jsonPath("$.results", hasSize(5)));

	}
	
	@Test
	public void getProducts_fiftering() throws Exception {

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.get("/products")
				.param("search", "B")
				.param("category", "CAR");

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.limit", notNullValue())).andExpect(jsonPath("$.offset", notNullValue()))
				.andExpect(jsonPath("$.total", notNullValue())).andExpect(jsonPath("$.results", hasSize(2)));

	}
	
	@Test
	public void getProducts_sorting() throws Exception {

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.get("/products")
				.param("orderBy", "price")
				.param("sort", "desc");

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.limit", notNullValue()))
				.andExpect(jsonPath("$.offset", notNullValue()))
				.andExpect(jsonPath("$.total", notNullValue()))
				.andExpect(jsonPath("$.results", hasSize(5)))
				.andExpect(jsonPath("$.results[0].productId", equalTo(6)))
				.andExpect(jsonPath("$.results[1].productId", equalTo(5)))
				.andExpect(jsonPath("$.results[2].productId", equalTo(7)))
				.andExpect(jsonPath("$.results[3].productId", equalTo(4)))
				.andExpect(jsonPath("$.results[4].productId", equalTo(2)));
	}
	
	@Test
	public void getProducts_pageination() throws Exception {
		RequestBuilder reuestBuilder = MockMvcRequestBuilders.get("/products")
				.param("limit", "2")
				.param("offset", "2");

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.limit", notNullValue()))
				.andExpect(jsonPath("$.offset", notNullValue()))
				.andExpect(jsonPath("$.total", notNullValue()))
				.andExpect(jsonPath("$.results", hasSize(2)))
				.andExpect(jsonPath("$.results[0].productId", equalTo(5)))
				.andExpect(jsonPath("$.results[1].productId", equalTo(4)));
	
	
	}
}
