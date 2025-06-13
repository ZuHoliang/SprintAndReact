package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.ShiftSwapRequest;
import com.example.demo.model.entity.User;
import com.example.demo.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	public void sendSwapRequestNotification(User targetUser, ShiftSwapRequest request) {
		System.out.printf("[通知] %s 收到來自 %s 的換班請求: %s %s /n",
				targetUser.getUsername(),
                request.getRequestUser().getUsername(),
                request.getSwapDate()
                );
		
	}

	@Override
	public void sendSwapResponseNotification(User requester, boolean approved, String message) {
		String result = approved ? "同意" : "拒絕";
		System.out.printf("[通知] 你的換班申請被%s，附加訊息：%s\n", result, message != null ? message : "無");
	}
	
	@Override
    public void sendSwapSuccessNotification(User requester, User target, ShiftSwapRequest request) {
        System.out.printf("[通知] 換班成功！%s ↔ %s，日期：%s\n",
                requester.getUsername(),
                target.getUsername(),
                request.getSwapDate()
                );
    }
	
}
