package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.SwapRequestNotFoundException;
import com.example.demo.model.dto.ShiftSwapRequestDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.ShiftSwapRequest;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.ShiftType;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.AuthService;
import com.example.demo.service.ScheduleService;
import com.example.demo.service.ShiftSwapRequestService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/swap")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8008" }, allowCredentials = "true")
public class ShiftSwapRequestRestController {
	
	@Autowired
	private ShiftSwapRequestService shiftSwapRequestService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserRepository userRepository;
	
	//換班申請
	@PostMapping("")
	public ResponseEntity<ApiResponse<ShiftSwapRequest>> requestSwap(@RequestBody ShiftSwapRequestDto dto
			, HttpSession session){
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
		User requester = userRepository.findById(cert.getUserId())
				.orElseThrow(() -> new SwapRequestNotFoundException("找不到使用者"));
		User target = null;
		if(dto.getTargetUserId() != null) {
			target = userRepository.findById(dto.getTargetUserId()).orElse(null);
		}
		LocalDate date = dto.getSwapDate();
		ShiftType shiftType = dto.getSwapToShift() != null? ShiftType.valueOf(dto.getSwapToShift()) : null;
		ShiftSwapRequest request = shiftSwapRequestService.requestSwap(requester, target, date, shiftType, dto.getSwapMessage());
		return ResponseEntity.ok(ApiResponse.success("已送出換班申請", request));
	}
	
	//同意換班
	@PostMapping("/{id}/approve")
	public ResponseEntity<ApiResponse<Void>> approveSwap(@PathVariable("id") Integer id, @RequestParam(required = false) String message, HttpSession session){
		authService.checkAuthenticated(session);
		shiftSwapRequestService.approveSwap(id, message);
		return ResponseEntity.ok(ApiResponse.success("已同意換班", null));
	}
	
	//拒絕換班
	@PostMapping("/{id}/reject")
	public ResponseEntity<ApiResponse<Void>> rejectSwap(@PathVariable("id") Integer id, @RequestParam(required = false) String message, HttpSession session){
		authService.checkAuthenticated(session);
		shiftSwapRequestService.rejectSwap(id, message);
		return ResponseEntity.ok(ApiResponse.success("已拒絕換班", null));
	}
	
	//取消換班
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> cancelSwap(@PathVariable("id") Integer id, HttpSession session){
		authService.checkAuthenticated(session);
		shiftSwapRequestService.cancelSwap(id);
		return ResponseEntity.ok(ApiResponse.success("已取消換班", null));
	}
	
	//查詢收到的請求
	@GetMapping("/received")
	public ResponseEntity<ApiResponse<List<ShiftSwapRequest>>> getReceivedRequests(HttpSession session){
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
		User user = userRepository.findById(cert.getUserId())
				.orElseThrow(() -> new SwapRequestNotFoundException("找不到使用者"));
		List<ShiftSwapRequest> list = shiftSwapRequestService.getRequestsSentByUser(user);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", list));
	}
	
	//查詢送出的請求
	@GetMapping("/sent")
	public ResponseEntity<ApiResponse<List<ShiftSwapRequest>>> getSentRequests(HttpSession session){
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
		User user = userRepository.findById(cert.getUserId())
				.orElseThrow(() -> new SwapRequestNotFoundException("找不到使用者"));
		List<ShiftSwapRequest> list = shiftSwapRequestService.getRequestsReceivedByUser(user);
		return ResponseEntity.ok(ApiResponse.success("查詢成功", list));
	}

}
