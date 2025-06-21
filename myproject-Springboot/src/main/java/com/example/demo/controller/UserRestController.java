package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.dto.AccountManageDto;
import com.example.demo.model.dto.PersonalEditDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.dto.UserDto;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8008" }, allowCredentials = "true")
public class UserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthService authService;

	// 取得全部使用者(ADMID限定)
	@GetMapping("/admin/users")
	public List<UserDto> getAllUsers(HttpSession session) {
		authService.checkAdminPermission(session);
		return userService.getAllUsers();
	}

	// 取得個人資料(登入後使用)
	@GetMapping("/users/me")
	public UserDto getCurrentUser(HttpSession session) {
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
		return userService.getUser(cert.getAccountId());
	}

	// 更新個人資料(登入後使用)
	@PutMapping("/users/me")
	public UserDto updateCurrentUser(@Valid @RequestBody PersonalEditDto dto, HttpSession session) {
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
		
		String password = dto.getPassword();
		if (password != null && password.isBlank()) {
			password = null;
		}
		
		return userService.updateUser(cert.getUserId(), dto.getUsername(), password);
	}
	
	
	// 修改密碼(ADMID限定)
	@PutMapping("/admin/users/{accountId}/password")
	public UserDto updatePassword(@PathVariable String accountId, @RequestBody AccountManageDto dto,
			HttpSession session) {
		authService.checkAdminPermission(session);
		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密碼不可為空");
		}
		return userService.updatePassword(accountId, dto.getPassword());
	}

	// 修改權限(ADMID限定)
	@PutMapping("/admin/users/{accountId}/role")
	public UserDto updateRole(@PathVariable String accountId, @RequestBody AccountManageDto dto, HttpSession session) {
		authService.checkAdminPermission(session);
		if (dto.getRole() == null || (dto.getRole() != 1 && dto.getRole() != 2)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "權限錯誤");
		}
		return userService.updateRole(accountId, dto.getRole());
	}

	// 修改狀態(ADMID限定)
	@PutMapping("/admin/users/{accountId}/active")
	public UserDto updateActive(@PathVariable String accountId, @RequestBody AccountManageDto dto,
			HttpSession session) {
		authService.checkAdminPermission(session);
		if (dto.getActive() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "狀態");
		}
		return userService.updateActive(accountId, dto.getActive());
	}
	
	// 新增使用者(ADMIN限定)
	@PostMapping("/admin/users")
	public UserDto AddUser(@Valid @RequestBody AccountManageDto dto, HttpSession session) {
		authService.checkAdminPermission(session);
		return userService.addUser(dto.getUsername(), dto.getPassword(), dto.getRole(), dto.getActive());
	}

}
