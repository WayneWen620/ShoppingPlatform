package com.steven.service;

import com.steven.dto.UserLoginRequest;
import com.steven.dto.UserRegisterRequest;
import com.steven.model.User;

import jakarta.validation.Valid;

public interface UserService {

	Integer register(@Valid UserRegisterRequest request);

	User getUserById(Integer userId);

	User login(@Valid UserLoginRequest request);

}
