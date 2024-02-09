package com.steven.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.steven.constant.ProductCategory;
import com.steven.dao.ProductDao;
import com.steven.dto.ProductQueryParams;
import com.steven.dto.ProductRequest;
import com.steven.model.Product;
import com.steven.rowmapper.ProductRowMapper;

import jakarta.validation.Valid;
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

	@Override
	public Integer createProduct(@Valid ProductRequest request) {
		String sql="insert into product(product_name,category,image_url,price,stock,description,create_date,last_modified_date)\r\n"
				+ "values(:productName,:category,:imageUrl,:price,:stock,:description,:createDate,:lastModifiedDate)";
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("productName", request.getProductName());
		map.put("category", request.getCategory().toString());
		map.put("imageUrl", request.getImageUrl());
		map.put("price", request.getPrice());
		map.put("stock", request.getStock());
		map.put("description", request.getDescription());

		Date now=new Date();
		
		map.put("createDate", now);
		map.put("lastModifiedDate", now);
		
		KeyHolder holder=new GeneratedKeyHolder();
		template.update(sql, new MapSqlParameterSource(map),holder);
		int productId=holder.getKey().intValue();
		
		
		return productId;
	}

	@Override
	public void updateProduct(Integer productId, @Valid ProductRequest request) {
		String sql="update product set product_name=:productName,category=:category,image_url=:imageUrl,price=:price,stock=:stock,description=:description,last_modified_date=:lastModifiedDate  where product_id=:productId";
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("productName", request.getProductName());
		map.put("category", request.getCategory().toString());
		map.put("imageUrl", request.getImageUrl());
		map.put("price", request.getPrice());
		map.put("stock", request.getStock());
		map.put("description", request.getDescription());

		map.put("lastModifiedDate", new Date());
		
		map.put("productId", productId);
		template.update(sql, new MapSqlParameterSource(map));
		
	}

	@Override
	public void deleteProduct(Integer productId) {
		String sql="delete from product where product_id=:productId";
		Map<String, Object>map=new HashMap<String, Object>();
		
		map.put("productId", productId);
		template.update(sql, new MapSqlParameterSource(map));
		
	}

	@Override
	public List<Product> getProducts(ProductQueryParams params){
		String sql="select product_id,product_name,category,image_url,price,stock,description,"
				+ "create_date,last_modified_date "
				+ "from product where 1=1";
		Map<String, Object>map=new HashMap<String, Object>();
		
		ProductCategory category =params.getCategory();
		String search =params.getSearch();
		String sort=params.getSort();
		String orderBy=params.getOrderBy();
		
		if(category!=null) {
			sql= sql+" and category=:category";
			map.put("category", category.name());
		}
		if(search!=null) {
			sql= sql+" and product_name like :search";
			map.put("search", "%"+search+"%");
		}
		sql= sql+" ORDER BY "+orderBy+" "+sort;
		
		List<Product> productList= template.query(sql, map,new ProductRowMapper());
		if(productList.size()>0) {
			return productList;
		}else {			
			return null;
		}
	}

}
