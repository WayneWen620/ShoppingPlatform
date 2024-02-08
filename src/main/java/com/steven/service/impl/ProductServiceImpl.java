package com.steven.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.steven.dao.ProductDao;
import com.steven.model.Product;
import com.steven.service.productService;
@Service
public class ProductServiceImpl implements productService{
	@Autowired
	private ProductDao productDao;
	@Override
	public Product getProductById(Integer productId) {
		
		return productDao.getProductById(productId);
	}

}
