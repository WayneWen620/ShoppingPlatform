package com.steven.service;

import java.util.List;

import com.steven.dto.CreateOrderRequest;
import com.steven.dto.OrderQueryParams;
import com.steven.model.Order;

import jakarta.validation.Valid;

public interface OrderService {

	Integer createOrder(Integer userId, @Valid CreateOrderRequest createOrderRequest);

	Order getOrderById(Integer orderId);

	List<Order> getOrders(OrderQueryParams orderQueryParams);

	Integer countOrder(OrderQueryParams orderQueryParams);

}
