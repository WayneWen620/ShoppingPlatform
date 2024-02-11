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

import com.steven.dao.UserDao;
import com.steven.dto.UserRegisterRequest;
import com.steven.model.User;
import com.steven.rowmapper.UserRowMapper;

import jakarta.validation.Valid;
@Component
public class UserDaoImpl implements UserDao{
	@Autowired
	private NamedParameterJdbcTemplate template;

	@Override
	public Integer createUser(@Valid UserRegisterRequest request) {
		String sql = " insert into user(email,password,created_date,last_modified_date)"
				+" values(:email,:password,:createdDate,:lastModifiedDate)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", request.getEmail());
		map.put("password", request.getPassword());

		Date now = new Date();

		map.put("createdDate", now);
		map.put("lastModifiedDate", now);

		KeyHolder holder = new GeneratedKeyHolder();
		template.update(sql, new MapSqlParameterSource(map), holder);
		int userId = holder.getKey().intValue();
		return userId;
	}

	@Override
	public User getUserById(Integer userId) {
		String sql = "select user_id,email,password,created_date,last_modified_date " 
				+ "from user where user_id=:userId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);

		List<User> userList = template.query(sql, map, new UserRowMapper());
		if (userList.size() > 0) {
			return userList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public User getUserByEmail(String email) {
		String sql = "select user_id,email,password,created_date,last_modified_date " 
				+ "from user where email=:email";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);

		List<User> userList = template.query(sql, map, new UserRowMapper());
		if (userList.size() > 0) {
			return userList.get(0);
		} else {
			return null;
		}
	}

}
