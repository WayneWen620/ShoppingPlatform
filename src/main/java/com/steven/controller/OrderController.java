package com.steven.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.steven.dto.CreateOrderRequest;
import com.steven.dto.OrderQueryParams;
import com.steven.model.Order;
import com.steven.service.OrderService;
import com.steven.util.Page;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/users/{userId}/orders")
	public ResponseEntity<?> createOrder(@PathVariable Integer userId,
			@RequestBody @Valid CreateOrderRequest createOrderRequest) {
		
		Integer orderId=orderService.createOrder(userId,createOrderRequest);
		
		Order order=orderService.getOrderById(orderId);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(order);
		
	}
	
	@GetMapping("/users/{userId}/orders")
	public ResponseEntity<Page<Order>> getOrders(@PathVariable Integer userId,
			@RequestParam(defaultValue = "10") @Max(1000)@Min(0)Integer limit,
			@RequestParam(defaultValue = "0") @Min(0)Integer offset) {
		
		OrderQueryParams orderQueryParams=new OrderQueryParams();
		orderQueryParams.setLimit(limit);
		orderQueryParams.setOffset(offset);
		orderQueryParams.setUserId(userId);
		
		//取得order list

		List<Order> order=orderService.getOrders(orderQueryParams);
		
		//取得數量
		
		Integer count=orderService.countOrder(orderQueryParams);
		
		Page<Order>pages=new Page<>();
		pages.setLimit(limit);
		pages.setOffset(offset);
		pages.setResults(order);
		pages.setTotal(count);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(pages);
		
	}
}
