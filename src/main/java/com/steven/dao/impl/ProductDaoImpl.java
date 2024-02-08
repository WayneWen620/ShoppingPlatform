package com.steven.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.steven.dao.ProductDao;
import com.steven.model.Product;
import com.steven.rowmapper.ProductRowMapper;
@Component
public class ProductDaoImpl implements ProductDao{
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Override
	public Product getProductById(Integer productId) {
		String sql="select product_id,product_name,category,image_url,price,stock,description,"
				+ "create_date,last_modified_date "
				+ "from shoppingMall.product where product_id=:productId;";
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("productId", productId);
		
		List<Product> productList= template.query(sql, map,new ProductRowMapper());
		if(productList.size()>0) {
			return productList.get(0);
		}else {			
			return null;
		}
	}

}
