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

import com.steven.dao.OrderDao;
import com.steven.dto.OrderQueryParams;
import com.steven.model.Order;
import com.steven.model.OrderItem;
import com.steven.rowmapper.OrderItemRowMapper;
import com.steven.rowmapper.OrderRowMapper;

@Component
public class OrderDaoImpl implements OrderDao{
	@Autowired
	private NamedParameterJdbcTemplate template;


	@Override
	public Integer createOrder(Integer userId, int totalAmount) {
		String sql = "insert into `order`(user_id,total_amount,created_date,last_modified_date) "
				+ "values(:userId,:totalAmount,:createdDate,:lastModifiedDate)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("totalAmount", totalAmount);

		Date now = new Date();

		map.put("createdDate", now);
		map.put("lastModifiedDate", now);

		KeyHolder holder = new GeneratedKeyHolder();
		template.update(sql, new MapSqlParameterSource(map), holder);
		int productId = holder.getKey().intValue();

		return productId;
	}


	@Override
	public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
		
//		for(OrderItem orderItem: orderItemList) {
//			String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount) "
//					+ "values(:orderId,:productId,:quantity,:amount)";
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("orderId", orderId);
//			map.put("amount", orderItem.getAmount());
//			map.put("productId", orderItem.getProductId());
//			map.put("quantity", orderItem.getQuantity());
//			
//			template.update(sql, map);
//		}
		
		//批次
		String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount) "
				+ "values(:orderId,:productId,:quantity,:amount)";
		
		MapSqlParameterSource[] parameterSources=new MapSqlParameterSource[orderItemList.size()];
		for(int i=0; i<orderItemList.size();i++) {
			OrderItem orderItem=orderItemList.get(i);
			
			parameterSources[i]=new MapSqlParameterSource();
			parameterSources[i].addValue("orderId", orderId);
			parameterSources[i].addValue("amount", orderItem.getAmount());
			parameterSources[i].addValue("productId", orderItem.getProductId());
			parameterSources[i].addValue("quantity", orderItem.getQuantity());
		}
		template.batchUpdate(sql, parameterSources);

	}


	@Override
	public Order getOrderById(Integer orderId) {
		String sql = "select order_id,user_id,total_amount,created_date,last_modified_date " 
				+ "from `order` where order_id=:orderId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);

		List<Order> orderList = template.query(sql, map, new OrderRowMapper());
		if (orderList.size() > 0) {
			return orderList.get(0);
		} else {
			return null;
		}
	}


	@Override
	public List<OrderItem> getOrderItemByOrderId(Integer orderId) {
		String sql = "\r\n"
				+ "select item.order_item_id,item.order_id, item.product_id, item.quantity, item.amount ,p.product_name ,p.image_url "
				+ " from order_item  item "
				+ "Left join product p on item.product_id=p.product_id "
				+ "where item.order_id=:orderId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);

		List<OrderItem> orderItemList = template.query(sql, map, new OrderItemRowMapper());
		return orderItemList;
	}


	@Override
	public List<Order> getOrders(OrderQueryParams orderQueryParams) {
		String sql = "select order_id,user_id,total_amount,created_date,last_modified_date " 
				+ "from `order` where 1=1";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//查詢條件
		
		sql=addFilteringSql(sql,map,orderQueryParams);
		
		//排序
		sql=sql+" Order by created_date DESC";
		
		//分頁
		sql=sql+" LIMIT :limit OFFSET :offset";
		map.put("offset", orderQueryParams.getOffset());
		map.put("limit", orderQueryParams.getLimit());
		List<Order> orderList=template.query(sql, map,new OrderRowMapper());
		return orderList;
	}


	@Override
	public Integer countOrder(OrderQueryParams orderQueryParams) {
		
		
		String sql = "select count(*) from `order` where 1=1";
		Map<String, Object> map = new HashMap<String, Object>();
		sql=addFilteringSql(sql,map,orderQueryParams);
		
		Integer total= template.queryForObject(sql, map, Integer.class);
		
		return total;
	}


	private String addFilteringSql(String sql, Map<String, Object> map, OrderQueryParams orderQueryParams) {
		if(orderQueryParams.getUserId()!=null) {
			sql =sql+" AND user_id=:userId";
			map.put("userId", orderQueryParams.getUserId());
		}
		return sql;
	}





	
}
