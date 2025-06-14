package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.entity.Notification;
import com.example.demo.model.entity.ShiftSwapRequest;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.NotificationType;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	public void sendSwapRequestNotification(User targetUser, ShiftSwapRequest request) {
		System.out.printf("[通知] %s 收到來自 %s 的換班請求: %s %s\n", targetUser.getUsername(),
				request.getRequestUser().getUsername(), request.getSwapDate(), request.getSwapToShift());

	}

	@Override
	public void sendSwapResponseNotification(User requester, boolean approved, String message) {
		NotificationType type = approved ? NotificationType.SWAP_APPROVED : NotificationType.SWAP_REJECTED;
		String text = approved ? "換班申請已通過" : "換班申請被拒絕";
		if (message != null && !message.isEmpty()) {
			text = "訊息:" + message;
		}
		Notification n = new Notification();
		n.setRecipient(requester);
		n.setType(type);
		n.setText(text);
		notificationRepository.save(n);

	}

	@Override
	public void sendSwapSuccessNotification(User requester, User target, ShiftSwapRequest request) {
		System.out.printf("[通知] 換班成功！%s ↔ %s，日期：%s\n", requester.getUsername(), target.getUsername(),
				request.getSwapDate());
	}

	@Override
	public List<Notification> getNotifications(User user) {
		return notificationRepository.findByRecipientOrderByCreatedTimeDesc(user);
	}

	@Override
	public void deleteNotification(User user, Long id) {
		Notification n = notificationRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "通知不存在"));
		if (!n.getRecipient().getUserId().equals(user.getUserId())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "無權刪除該通知");
		}
		notificationRepository.delete(n);
	}
}
