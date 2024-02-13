package com.steven.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.steven.model.OrderItem;

public class OrderItemRowMapper implements RowMapper<OrderItem> {

	@Override
	public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrderItem orderItem = new OrderItem();
		orderItem.setAmount(rs.getInt("amount"));
		orderItem.setOrderId(rs.getInt("order_id"));
		orderItem.setOrderItemId(rs.getInt("order_item_id"));
		orderItem.setProductId(rs.getInt("product_id"));
		orderItem.setQuantity(rs.getInt("quantity"));
		
		orderItem.setProductName(rs.getString("product_name"));
		orderItem.setImageUrl(rs.getString("image_url"));
	return orderItem;
		
		
	
	}

	

}
