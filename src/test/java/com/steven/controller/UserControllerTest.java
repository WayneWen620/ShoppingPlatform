package com.steven.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steven.dao.UserDao;
import com.steven.dto.UserLoginRequest;
import com.steven.dto.UserRegisterRequest;
import com.steven.model.User;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserDao userDao;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@Transactional
	public void register_success() throws Exception {
		UserRegisterRequest request = new UserRegisterRequest();
		request.setEmail("test123_7@gmail.com");
		request.setPassword("1234");

		String json = objectMapper.writeValueAsString(request);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.post("/users/register")
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(201))
				.andExpect(jsonPath("$.userId", notNullValue()))
				.andExpect(jsonPath("$.email", equalTo(request.getEmail())))
				.andExpect(jsonPath("$.createdDate", notNullValue()))
				.andExpect(jsonPath("$.lastModifiedDate", notNullValue()));

		// 檢查資料庫中的密碼不為名碼
		User user = userDao.getUserByEmail(request.getEmail());
		assertNotEquals(request.getPassword(), user.getPassword());

	}

	@Test
	@Transactional
	public void register_invaliEmailFormat() throws Exception {
		UserRegisterRequest request = new UserRegisterRequest();
		request.setEmail("test123_");
		request.setPassword("12342333");

		String json = objectMapper.writeValueAsString(request);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.post("/users/register")
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(400));

	}

	@Test
	@Transactional
	public void register_emailAlreadyExist() throws Exception {
		UserRegisterRequest request = new UserRegisterRequest();
		request.setEmail("test2@gmail.com");
		request.setPassword("123");

		String json = objectMapper.writeValueAsString(request);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.post("/users/register")
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(201));

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(400));

	}

	// 登入功能
	@Test
	@Transactional
	public void login_success() throws Exception {
		// 先註冊
		UserRegisterRequest request = new UserRegisterRequest();
		request.setEmail("test123_6@gmail.com");
		request.setPassword("123");

		register(request);

		// 測試登入功能

		UserLoginRequest loginRequest = new UserLoginRequest();
		loginRequest.setEmail(request.getEmail());
		loginRequest.setPassword(request.getPassword());

		String json = objectMapper.writeValueAsString(loginRequest);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.post("/users/login")
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(200));

	}

	// 輸入錯誤密碼
	@Test
	@Transactional
	public void login_wrongPassword() throws Exception {
		// 先註冊
		UserRegisterRequest request = new UserRegisterRequest();
		request.setEmail("test8@gmail.com");
		request.setPassword("123");

		register(request);

		// 測試登入功能

		UserLoginRequest loginRequest = new UserLoginRequest();
		loginRequest.setEmail(request.getEmail());
		loginRequest.setPassword("Unknown");

		String json = objectMapper.writeValueAsString(loginRequest);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.post("/users/login")
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(400));

	}

	// 輸入錯誤密碼格式
	@Transactional
	@Test
	public void login_invalidEmailFormat() throws Exception {
		// 先註冊
		UserRegisterRequest request = new UserRegisterRequest();
		request.setEmail("test2");
		request.setPassword("123");

		String json = objectMapper.writeValueAsString(request);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.post("/users/login")
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(400));

	}
	@Transactional
	@Test
	public void login_emailNotExist() throws Exception {

		// 測試登入功能

		UserLoginRequest loginRequest = new UserLoginRequest();
		loginRequest.setEmail("unknown@gmail.com");
		loginRequest.setPassword("123");

		String json = objectMapper.writeValueAsString(loginRequest);

		RequestBuilder reuestBuilder = MockMvcRequestBuilders.post("/users/login")
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(reuestBuilder).andDo(print()).andExpect(status().is(400));

	}

	private void register(UserRegisterRequest userRegisterRequest) throws Exception {
		String json = objectMapper.writeValueAsString(userRegisterRequest);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/register")
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(requestBuilder).andExpect(status().is(201));
	}
	/*
	 * 
	 * @PostMapping("/users/register") public ResponseEntity<User>
	 * register(@RequestBody @Valid UserRegisterRequest request){
	 * 
	 * Integer userId= userService.register(request);
	 * 
	 * User user=userService.getUserById(userId);
	 * 
	 * 
	 * return ResponseEntity.status(HttpStatus.CREATED).body(user); }
	 * 
	 * @PostMapping("/users/login") public ResponseEntity<User>
	 * login(@RequestBody @Valid UserLoginRequest request){
	 */

}
