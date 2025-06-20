package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.UserDto;

public interface UserService {
	public UserDto getUser(String accountId);	

	public List<UserDto> getAllUsers();
	
	public UserDto updateUser(Integer userId, String username, String password);

	public UserDto addUser(String userName, String password, Integer role, Boolean active);

	UserDto updatePassword(String accountId, String password);

	UserDto updateRole(String account, Integer role);

	UserDto updateActive(String accountId, Boolean active);

}
