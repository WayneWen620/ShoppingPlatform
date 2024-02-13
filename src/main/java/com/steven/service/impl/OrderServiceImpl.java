package com.steven.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.steven.dao.OrderDao;
import com.steven.dao.ProductDao;
import com.steven.dao.UserDao;
import com.steven.dto.BuyItem;
import com.steven.dto.CreateOrderRequest;
import com.steven.dto.OrderQueryParams;
import com.steven.model.Order;
import com.steven.model.OrderItem;
import com.steven.model.Product;
import com.steven.model.User;
import com.steven.service.OrderService;

import jakarta.validation.Valid;

@Service
public class OrderServiceImpl implements OrderService{
	
	private final static Logger log=LoggerFactory.getLogger(OrderService.class);
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private UserDao userDao;
	
	@Transactional
	@Override
	public Integer createOrder(Integer userId, @Valid CreateOrderRequest createOrderRequest) {
		//檢查user是否存在
		User user=userDao.getUserById(userId);
		
		if(user==null) {
			log.warn("該 userId:{}不存在",userId);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		//訂單總花費金額
		int totalAmount=0;
		List<OrderItem> orderItemList=new ArrayList<OrderItem>();
		
		for(BuyItem item:createOrderRequest.getBuyItemList()) {
			
			Product product=productDao.getProductById(item.getProductId());
			
			
			
			//檢查庫存
			if(product==null) {
				log.warn("商品:{}不存在",item.getProductId());
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
			}else if (product.getStock()<item.getQuantity()) {
				log.warn("商品:{}庫存不足，無法購買，蹭於庫存{}，欲購買庫存:{}",item.getProductId(),product.getStock(),item.getQuantity());
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

			}
			
			//扣除庫存
			productDao.updateProduct(product.getProductId(), product.getStock()-item.getQuantity());
			
			
			//計算價錢
			int amount=item.getQuantity() *product.getPrice();
			totalAmount=totalAmount+amount;
			
			//轉換orderItem
			OrderItem orderItem=new OrderItem();
			orderItem.setAmount(amount);
			orderItem.setProductId(item.getProductId());
			orderItem.setQuantity(item.getQuantity());
			orderItemList.add(orderItem);
		}
		
		
		//創建訂單
		Integer orderId= orderDao.createOrder(userId,totalAmount);
		
		orderDao.createOrderItems(orderId,orderItemList);
		return orderId;
	}
	@Override
	public Order getOrderById(Integer orderId) {
		Order order= orderDao.getOrderById(orderId);
		List<OrderItem> orderItems=orderDao.getOrderItemByOrderId(orderId);
		order.setOrderItemList(orderItems);
		return order;
	}
	@Override
	public List<Order> getOrders(OrderQueryParams orderQueryParams) {
		List<Order> orderList=orderDao.getOrders(orderQueryParams);
		
		for(Order order:orderList) {
			List<OrderItem> orderItems=orderDao.getOrderItemByOrderId(order.getOrderId());
			order.setOrderItemList(orderItems);
		}
		
		return orderList;
	}
	@Override
	public Integer countOrder(OrderQueryParams orderQueryParams) {
		// TODO Auto-generated method stub
		return orderDao.countOrder(orderQueryParams);
	}
}
