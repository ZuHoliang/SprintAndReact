package com.example.demo.controller;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.mapper.ScheduleMapper;
import com.example.demo.model.dto.ScheduleDto;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.Schedule;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.ShiftType;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.AuthService;
import com.example.demo.service.ScheduleService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8008" }, allowCredentials = "true")
public class ScheduleRestController {
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private ScheduleMapper scheduleMapper;
	
	private User buildUser(Integer userId) {
		User user = new User();
		user.setUserId(userId);
		return user;
	}
	
	//排班
	@PostMapping("")
	public ResponseEntity<ApiResponse<ScheduleDto>> assignShift(@RequestParam String date, @RequestParam String shiftType
			, HttpSession session){
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
		LocalDate workUDate = LocalDate.parse(date);
		ShiftType shift = ShiftType.valueOf(shiftType);
		Schedule schedule = scheduleService.assignShift(buildUser(cert.getUserId()), workUDate, shift);
		ScheduleDto dto = scheduleMapper.toDto(schedule);
		return ResponseEntity.ok(ApiResponse.success("排班成功", dto));
	}
	
	//取消排班
	@DeleteMapping("")
	public ResponseEntity<ApiResponse<Void>> cancelShift(@RequestParam String date, @RequestParam String shiftType
			, HttpSession session){
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
		LocalDate workDate = LocalDate.parse(date);
		ShiftType shift = ShiftType.valueOf(shiftType);
		scheduleService.cancelShift(buildUser(cert.getUserId()), workDate, shift);
		return ResponseEntity.ok(ApiResponse.success("取消排班成功", null));
	}
	
	//取得個人整月排班
	@GetMapping("/me/month")
	public ResponseEntity<ApiResponse<List<ScheduleDto>>> getMyMonthSchedules(HttpSession session){
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
		LocalDate now = LocalDate.now();
		List<Schedule> schedules = scheduleService.getUserMonthlySchedules(buildUser(cert.getUserId()), now.getMonthValue(), now.getYear());
		List<ScheduleDto> dtos = schedules.stream().map(scheduleMapper::toDto).collect(Collectors.toList());
		return ResponseEntity.ok(ApiResponse.success("查詢成功", dtos));
	}
	
	//各得本月剩餘班表
	@GetMapping("/me/remaining")
	public ResponseEntity<ApiResponse<List<ScheduleDto>>> getMyRemainingSchedules(HttpSession session){
		authService.checkAuthenticated(session);
		UserCert cert = (UserCert) session.getAttribute("userCert");
		LocalDate now = LocalDate.now();
		LocalDate end = now.with(TemporalAdjusters.lastDayOfMonth());
		List<Schedule> schedules = scheduleService.getUserRemainingSchedules(buildUser(cert.getUserId()), now, end);
		List<ScheduleDto> dtos = schedules.stream().map(scheduleMapper::toDto).collect(Collectors.toList());
		return ResponseEntity.ok(ApiResponse.success("查詢成功", dtos));
	}
	
	//取得本月班表
	@GetMapping("/{year}/{month}")
	public ResponseEntity<ApiResponse<List<ScheduleDto>>> getMonthSchedules(@PathVariable int year, @PathVariable int month){
		List<Schedule> schedules = scheduleService.getMonthSchedules(year, month);
		List<ScheduleDto> dtos = schedules.stream().map(scheduleMapper::toDto).collect(Collectors.toList());
		return ResponseEntity.ok(ApiResponse.success("查詢成功", dtos));
	}	
	
}
