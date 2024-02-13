package com.steven.dao;

import java.util.List;

import com.steven.dto.OrderQueryParams;
import com.steven.model.Order;
import com.steven.model.OrderItem;

public interface OrderDao {

	Integer createOrder(Integer userId, int totalAmount);

	void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

	Order getOrderById(Integer orderId);

	List<OrderItem> getOrderItemByOrderId(Integer orderId);

	List<Order> getOrders(OrderQueryParams orderQueryParams);

	Integer countOrder(OrderQueryParams orderQueryParams);

}
