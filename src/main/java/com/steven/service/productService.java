package com.steven.service;
import java.util.List;

import com.steven.constant.ProductCategory;
import com.steven.dto.ProductRequest;
import com.steven.model.Product;

import jakarta.validation.Valid;
public interface productService {
	Product getProductById(Integer productId);

	Integer createProduct(@Valid ProductRequest request);

	void updateProduct(Integer productId, @Valid ProductRequest request);

	void deleteProduct(Integer productId);

	List<Product> getProducts(ProductCategory category, String search);
}
