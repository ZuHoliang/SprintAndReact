package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.AnnouncementDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.AnnouncementService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8008" }, allowCredentials = "true")
public class HomeRestController {

	@Autowired
	private AnnouncementService announcementService;

	// 提供首頁顯示的最新公告（左側）
	@GetMapping("/latest-announcements")
	public ResponseEntity<ApiResponse<List<AnnouncementDto>>> getLatestAnnouncements() {
		List<AnnouncementDto> announcements = announcementService.getLatestAnnouncements();
		return ResponseEntity.ok(ApiResponse.success("最新公告查詢成功", announcements));
	}

	// 提供 session 中登入使用者資訊（右側）
	@GetMapping("/session-user")
	public ResponseEntity<ApiResponse<Object>> getSessionUserInfo(HttpSession session) {
		Object cert = session.getAttribute("userCert");

		if (cert instanceof UserCert userCert) {
			return ResponseEntity.ok(ApiResponse.success("使用者已登入", userCert));
		} else {
			return ResponseEntity.ok(ApiResponse.success("尚未登入", null));
		}
	}
}
