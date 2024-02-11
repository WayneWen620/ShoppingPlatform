package com.steven.service.impl;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import com.steven.dao.UserDao;
import com.steven.dto.UserLoginRequest;
import com.steven.dto.UserRegisterRequest;
import com.steven.model.User;
import com.steven.service.UserService;

import jakarta.validation.Valid;
@Service
public class UserServiceImpl implements UserService{
	
	private final static Logger log=LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserDao userDao;
	
	@Override
	public Integer register(@Valid UserRegisterRequest request) {
		User user=userDao.getUserByEmail(request.getEmail());
		if(user!=null) {
			log.warn("該email{}已被註冊",request.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		//使用md5 生成密碼雜湊值
		String hashedPassword=DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
		request.setPassword(hashedPassword);

		return userDao.createUser(request);			
		
	}

	@Override
	public User getUserById(Integer userId) {
		return userDao.getUserById(userId);
	}

	@Override
	public User login(@Valid UserLoginRequest request) {
		User user=userDao.getUserByEmail(request.getEmail());
		//檢查user是否存在
		if(user==null) {
			log.warn("該email{}尚未註冊",request.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		}
		//使用md5生成密碼雜湊值
		String hashPassword=DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
		
		//比較密碼
		if(user.getPassword().equals(hashPassword)) {
			return user;
		}else {
			log.warn("email{} 密碼帳號不正確",request.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		}
	}

}
