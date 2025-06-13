package com.example.demo.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.exception.DuplicateScheduleException;
import com.example.demo.exception.ScheduleFullException;
import com.example.demo.exception.SwapRequestNotFoundException;
import com.example.demo.exception.SwapRequestStateException;
import com.example.demo.model.entity.Schedule;
import com.example.demo.model.entity.ShiftSwapRequest;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.RequestStatus;
import com.example.demo.model.enums.ShiftType;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.repository.ShiftSwapRequestRepository;
import com.example.demo.service.NotificationService;
import com.example.demo.service.ShiftSwapRequestService;

import jakarta.transaction.Transactional;

@Service
public class ShiftSwapRequestServiceImpl implements ShiftSwapRequestService {

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private ShiftSwapRequestRepository shiftSwapRequestRepository;

	@Autowired
	private NotificationService notificationService;

	// 發送換班申請
	@Override
	public ShiftSwapRequest requestSwap(User requester, User target, LocalDate date, ShiftType shiftType, String note) {
		// 檢查申請人使否已經排班
		if (scheduleRepository.existsByWorkUserAndWorkDateAndShiftType(requester, date, shiftType)) {
			 throw new DuplicateScheduleException("已排班");
		}
		// 檢查是要換的班是否已滿
		int count = scheduleRepository.countByWorkDateAndShiftType(date, shiftType);
		if (count >= 2 && target == null) {
			throw new ScheduleFullException("班表已滿");
		}

		Schedule requesterSchedule = scheduleRepository.findByWorkUserAndWorkDate(requester, date);

		ShiftSwapRequest request = new ShiftSwapRequest();
		request.setRequestUser(requester);
		request.setTargetUser(target);
		request.setSwapDate(date);
        request.setSwapToShift(shiftType);
        request.setSwapFromShift(requesterSchedule != null ? requesterSchedule.getShiftType() : null);
		request.setSwapMessage(note);
		request.setReqStatus(RequestStatus.PENDING);

		ShiftSwapRequest saved = shiftSwapRequestRepository.save(request);

		if (target != null) {
			notificationService.sendSwapRequestNotification(target, saved);
		}

		return saved;
	}

	// 同意換班
	@Override
	@Transactional
	public void approveSwap(Integer requestId, String message) {
		ShiftSwapRequest request = shiftSwapRequestRepository.findById(requestId.longValue())
				.orElseThrow(() -> new SwapRequestNotFoundException("找不到換班請求"));

		if (request.getReqStatus() != RequestStatus.PENDING) {
			throw new SwapRequestStateException("請求狀態已更新");
		}

		request.setReqStatus(RequestStatus.APPROVED);
		if (message != null) {
			request.setRespMessage(message);
		}

		shiftSwapRequestRepository.save(request);

		LocalDate date = request.getSwapDate();
		User requester = request.getRequestUser();
		User target = request.getTargetUser();
		ShiftType from = request.getSwapFromShift();
		ShiftType to = request.getSwapToShift();

		// 同意後移除現有排班
		if (from != null) {
			scheduleRepository.deleteByWorkUserAndWorkDateAndShiftType(requester, date, from);
		}
		if (target != null && to != null) {
			scheduleRepository.deleteByWorkUserAndWorkDateAndShiftType(target, date, to);
		}

		// 加上新的排班
		if (to != null) {
			Schedule s = new Schedule();
			s.setWorkUser(requester);
			s.setWorkDate(date);
			s.setShiftType(to);
			scheduleRepository.save(s);
		}
		if (target != null && from != null) {
			Schedule s2 = new Schedule();
			s2.setWorkUser(target);
			s2.setWorkDate(date);
			s2.setShiftType(from);
			scheduleRepository.save(s2);
		}

		// 同意後發送通知
		notificationService.sendSwapResponseNotification(requester, true, message);
		if (target != null) {
			notificationService.sendSwapSuccessNotification(requester, target, request);
		}

	}

	// 拒絕換班
	@Override
	public void rejectSwap(Integer requestId, String message) {
		ShiftSwapRequest request = shiftSwapRequestRepository.findById(requestId.longValue())
				.orElseThrow(() -> new SwapRequestNotFoundException("找不到換班請求"));
		if (request.getReqStatus() != RequestStatus.PENDING) {
			throw new SwapRequestStateException("請求狀態已更新");
		}
		request.setReqStatus(RequestStatus.REJECTED);
		// 傳送拒絕訊息
		if (message != null) {
			request.setRespMessage(message);
		}
		shiftSwapRequestRepository.save(request);
		// 拒絕後發送通知
		notificationService.sendSwapResponseNotification(request.getRequestUser(), false, message);

	}

	// 取消換班
	@Override
	public void cancelSwap(Integer requestId) {
		ShiftSwapRequest request = shiftSwapRequestRepository.findById(requestId.longValue())
				.orElseThrow(() -> new SwapRequestNotFoundException("找不到換班請求"));
		request.setReqStatus(RequestStatus.CANCELLED);
		shiftSwapRequestRepository.save(request);
	}

	// 查詢已發出的請求
	@Override
	public List<ShiftSwapRequest> getRequestsSentByUser(User user) {
		return shiftSwapRequestRepository.findByRequestUser(user);
	}

	// 查詢收到的請求
	@Override
	public List<ShiftSwapRequest> getRequestsReceivedByUser(User user) {
		return shiftSwapRequestRepository.findByTargetUser(user);
	}

}
