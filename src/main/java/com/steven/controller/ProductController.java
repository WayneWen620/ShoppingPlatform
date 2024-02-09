package com.steven.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.steven.constant.ProductCategory;
import com.steven.dto.ProductQueryParams;
import com.steven.dto.ProductRequest;
import com.steven.model.Product;
import com.steven.service.productService;
import com.steven.util.Page;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
@Validated
@RestController
public class ProductController {

	@Autowired
	private productService productService;

	@GetMapping("/products")
	public ResponseEntity<Page<Product>> getProducts(
			//查詢條件
			@RequestParam(required = false) ProductCategory category,
			@RequestParam(required = false) String search,
			//排序
			@RequestParam(defaultValue = "create_date") String orderBy,
			@RequestParam(defaultValue = "desc") String sort,
			//分頁
			@RequestParam(defaultValue = "5") @Max(1000)@Min(0) Integer limit,
			@RequestParam(defaultValue =  "0")@Min(0) Integer offset
			) {

		ProductQueryParams params=new ProductQueryParams();
		params.setCategory(category);
		params.setSearch(search);
		params.setOrderBy(orderBy);
		params.setSort(sort);
		params.setLimit(limit);
		params.setOffset(offset);
		
		List<Product> listProduct = productService.getProducts(params);
		Integer total=productService.countProduct(params);
		
		Page<Product>page=new Page<>();
		page.setLimit(limit);
		page.setOffset(offset);
		page.setTotal(total);
		page.setResults(listProduct);
		
		return ResponseEntity.status(HttpStatus.OK).body(page);
		
	}

	
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
