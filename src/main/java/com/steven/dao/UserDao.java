package com.steven.dao;

import com.steven.dto.UserRegisterRequest;
import com.steven.model.User;

import jakarta.validation.Valid;

public interface UserDao {

	Integer createUser(@Valid UserRegisterRequest request);

	User getUserById(Integer userId);

	User getUserByEmail(String email);

}
