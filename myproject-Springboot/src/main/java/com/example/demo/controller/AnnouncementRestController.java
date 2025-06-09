package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.AnnouncementDto;
import com.example.demo.model.dto.AnnouncementEditDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.service.AnnouncementService;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8008" }, allowCredentials = "true")
public class AnnouncementRestController {

	@Autowired
	private AnnouncementService announcementService;

	@Autowired
	private AuthService authService;

	// 首頁(最新公告--權限0)
	@GetMapping("/latest")
	public List<AnnouncementDto> getLatestAnnouncements() {
		return announcementService.getLatestAnnouncements();
	}

	// 全部公告(權限0)
	@GetMapping("")
	public List<AnnouncementDto> getAllAnnouncements() {
		return announcementService.getAllAnnouncements();
	}

	// 指定公告
	@GetMapping("/{id}")
	public AnnouncementDto getAnnouncementDetail(@PathVariable Long id) {
		return announcementService.getAnnouncementById(id);
	}
	
	// 搜尋公告
	@GetMapping("/search")
	public List<AnnouncementDto> searchAnnouncements(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {
		return announcementService.searchAnnouncements(keyword, startDate, endDate);
	}

	// 新增公告(ADMIN限定 權限2)
	@PostMapping("/admin")
	public AnnouncementDto createAnnouncement(@Valid @RequestBody AnnouncementEditDto dto, HttpSession session) {
		authService.checkAdminPermission(session); // 檢查管理者權限
		UserCert cert = (UserCert) session.getAttribute("userCert");
		Integer authorId = cert.getUserId();
		return announcementService.createAnnouncement(dto, authorId);
	}

	// 更新公告(ADMIN限定 權限2)
	@PutMapping("/admin/{id}")
	public AnnouncementDto updateAnnouncement(@PathVariable Long id, @Valid @RequestBody AnnouncementEditDto dto,
			HttpSession session) {
		authService.checkAdminPermission(session); // 檢查管理者權限
		return announcementService.updateAnnouncement(id, dto);
	}

	// 隱藏公告(ADMIN限定 權限2)
	@PutMapping("/admin/{id}/active")
	public AnnouncementDto toggleActive(@PathVariable Long id, @RequestParam Boolean active, HttpSession session) {
		authService.checkAdminPermission(session);
		return announcementService.setAnnouncementActive(id, active);
	}

	// 刪除公告(ADMIN限定 權限2)
	@DeleteMapping("/admin/{id}")
	public void deleteAnnouncement(@PathVariable Long id, HttpSession session) {
		authService.checkAdminPermission(session);
		announcementService.deleteAnnouncement(id);
	}

}
