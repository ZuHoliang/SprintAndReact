package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Notification;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.AuthService;
import com.example.demo.service.NotificationService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8008" }, allowCredentials = "true")
public class NotificationRestController {
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("")
	public ResponseEntity<ApiResponse<List<Notification>>> getMyNotifications(HttpSession session) {
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
		User user = userRepository.findById(cert.getUserId()).orElse(null);
		List<Notification> list = notificationService.getNotifications(user);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", list));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long id, HttpSession session){
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
        User user = userRepository.findById(cert.getUserId()).orElse(null);
		notificationService.deleteNotification(user, id);
		return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
	}

}
