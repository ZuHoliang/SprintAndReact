package com.example.demo.service;

import com.example.demo.model.entity.ShiftSwapRequest;
import com.example.demo.model.entity.User;

public interface NotificationService {

	//發送換班請求通知
	void sendSwapRequestNotification(User targetUser, ShiftSwapRequest request);
    //發送回應通知
	void sendSwapResponseNotification(User requester, boolean approved, String message);
    //發送成功通知
	void sendSwapSuccessNotification(User requester, User target, ShiftSwapRequest request);

}
