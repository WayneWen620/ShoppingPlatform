package com.steven.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.steven.dao.ProductDao;
import com.steven.dto.ProductQueryParams;
import com.steven.dto.ProductRequest;
import com.steven.model.Product;
import com.steven.service.productService;

import jakarta.validation.Valid;
@Service
public class ProductServiceImpl implements productService{
	@Autowired
	private ProductDao productDao;
	@Override
	public Product getProductById(Integer productId) {
		
		return productDao.getProductById(productId);
	}
	@Override
	public Integer createProduct(@Valid ProductRequest request) {

		return productDao.createProduct(request);
	}
	@Override
	public void updateProduct(Integer productId, @Valid ProductRequest request) {
		 productDao.updateProduct(productId,request);
	}
	@Override
	public void deleteProduct(Integer productId) {
		productDao.deleteProduct(productId);
	}
	@Override
	public List<Product> getProducts(ProductQueryParams params) {
		return productDao.getProducts(params);
	}

}
