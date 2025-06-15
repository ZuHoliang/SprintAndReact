package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.model.entity.ShiftSwapRequest;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.ShiftType;

public interface ShiftSwapRequestService {

	// 發送換班申請
	ShiftSwapRequest requestSwap(User requester, User target, LocalDate date, ShiftType shiftType, String note);

	// 同意換班
	void approveSwap(Integer requestId, String message);

	// 拒絕換班
	void rejectSwap(Integer requestId, String message);

	// 取消換班
	void cancelSwap(Integer requestId);

	// 查詢已發出的請求
	List<ShiftSwapRequest> getRequestsSentByUser(User user);

	// 查詢收到的請求
	List<ShiftSwapRequest> getRequestsReceivedByUser(User user);

//	查詢整個月的換班申請(暫時不用)
//	List<ShiftSwapRequest> getRequestsByMonth(int year, int month);
}
