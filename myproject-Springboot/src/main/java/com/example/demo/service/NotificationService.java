package com.example.demo.service;

import java.util.List;

import com.example.demo.model.entity.Notification;
import com.example.demo.model.entity.ShiftSwapRequest;
import com.example.demo.model.entity.User;

public interface NotificationService {

	//發送換班請求通知
	void sendSwapRequestNotification(User targetUser, ShiftSwapRequest request);
    //發送回應通知
	void sendSwapResponseNotification(User requester, boolean approved, String message);
    //發送成功通知
	void sendSwapSuccessNotification(User requester, User target, ShiftSwapRequest request);
	//發送取消通知
	void sendSwapCancelNotification(User targetUser, ShiftSwapRequest request);	
	//刪除通知
	void deleteNotification(User user, Long id);
	//取得使用者通知
	List<Notification> getNotifications(User user);
	//即時調整通知數量
    void sendNotificationCountDelta(User user, int delta);	
	
}
