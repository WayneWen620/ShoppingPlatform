package com.steven.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.steven.dto.ProductRequest;
import com.steven.model.Product;
import com.steven.service.productService;

import jakarta.validation.Valid;

@RestController
public class ProductController {

	@Autowired
	private productService productService;

	@GetMapping("/product/{productId}")
	public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
		Product product = productService.getProductById(productId);

		if (product != null) {
			return ResponseEntity.status(HttpStatus.OK).body(product);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest request) {
		Integer productId = productService.createProduct(request);

		Product product = productService.getProductById(productId);

		return ResponseEntity.status(HttpStatus.CREATED).body(product);

	}

	@PutMapping("/products/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
			@Valid @RequestBody ProductRequest request) {
		Product product = productService.getProductById(productId);
		if (product != null) {
			productService.updateProduct(productId, request);

			Product updateProduct = productService.getProductById(productId);

			return ResponseEntity.status(HttpStatus.OK).body(updateProduct);

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		}

	}

	@DeleteMapping("/products/{productId}")
	public ResponseEntity<Product> deleteProduct(@PathVariable Integer productId) {

		productService.deleteProduct(productId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}
}
