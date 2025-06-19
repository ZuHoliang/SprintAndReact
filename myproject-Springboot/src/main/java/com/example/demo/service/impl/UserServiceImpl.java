package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.config.AIModerationConfig;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AIModerationService;
import com.example.demo.service.UserService;
import com.example.demo.util.Hash;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private AIModerationService aiModerationService;
	
	@Autowired
	private AIModerationConfig moderationConfig;

	// 找不到使用者
	private User findUser(String accountId) {
		User user = userRepository.getUser(accountId);
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到使用者: " + accountId);
		}
		return user;
	}

	// 取得使用者(單人)
	@Override
	public UserDto getUser(String accountId) {
		User user = userRepository.getUser(accountId);
		if (user == null) {
			return null;
		}
		return userMapper.toDto(user);
	}

	// 全部使用者表單
	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map(userMapper::toDto).collect(Collectors.toList());
	}

	// 新增使用者
	public UserDto addUser(String username, String password, Integer role, Boolean active) {
		if(moderationConfig.isUsernameCheck() && !aiModerationService.isAllowed(username)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "使用者名稱不當");
		}
		
		String maxAccountId = userRepository.findMaxAccountId(); // 取得目前最大accountId

		// 第一筆
		int nextId = 0;
		if (maxAccountId != null) {
			try {
				nextId = Integer.parseInt(maxAccountId) + 1;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				nextId = 0;
			}
		}

		String accountId = String.format("%04d", nextId);

		String salt = Hash.getSalt();
		String passwordHash = Hash.getPasswordHash(password, salt);
		User user = new User(null, accountId, username, passwordHash, salt, role, active);
		User saved = userRepository.save(user);
        return userMapper.toDto(saved);
	}

	// 更新使用者
	@Override
	public UserDto updateUser(Integer userId, String username, String password) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到使用者"));
		if (username != null) {
			if(moderationConfig.isUsernameCheck() && !aiModerationService.isAllowed(username)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "使用者名稱不當");
			}
			user.setUsername(username);
		}

		if (password != null) {
			String passwordHash = Hash.getPasswordHash(password, user.getSalt());
			user.setPasswordHash(passwordHash);
		}

		User saved = userRepository.save(user);
		return userMapper.toDto(saved);
	}

	// 修改密碼
	@Override
	public UserDto updatePassword(String accountId, String password) {
		User user = findUser(accountId);
		String salt = Hash.getSalt();
		String passwordHash = Hash.getPasswordHash(password, salt);
		user.setSalt(salt);
		user.setPasswordHash(passwordHash);
		User updated = userRepository.save(user);
		return userMapper.toDto(updated);

	}

	// 修改權限
	@Override
	public UserDto updateRole(String accountId, Integer role) {
		User user = findUser(accountId);
		user.setRole(role);
		User updated = userRepository.save(user);
		return userMapper.toDto(updated);
	}

	// 刪除員工(隱藏)
	@Override
	public UserDto updateActive(String accountId, Boolean active) {
		User user = findUser(accountId);
		user.setActive(active);
		User updated = userRepository.save(user);
		return userMapper.toDto(updated);
	}

}
