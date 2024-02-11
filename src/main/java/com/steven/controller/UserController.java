package com.steven.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.steven.dto.UserLoginRequest;
import com.steven.dto.UserRegisterRequest;
import com.steven.model.User;
import com.steven.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/users/register")
	public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest request){
		
		Integer userId= userService.register(request);
		
		User user=userService.getUserById(userId);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
	
	@PostMapping("/users/login")
	public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest request){
		
		User user= userService.login(request);
		

		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
}
