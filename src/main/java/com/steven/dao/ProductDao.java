package com.steven.dao;

import com.steven.dto.ProductRequest;
import com.steven.model.Product;

import jakarta.validation.Valid;

public interface ProductDao {
	Product getProductById(Integer productId);

	Integer createProduct(@Valid ProductRequest request);

	void updateProduct(Integer productId, @Valid ProductRequest request);

	void deleteProduct(Integer productId);
}
