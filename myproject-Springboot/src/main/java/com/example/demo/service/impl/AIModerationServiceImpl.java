package com.example.demo.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import com.example.demo.service.AIModerationService;

@Service
public class AIModerationServiceImpl implements AIModerationService {

	private final ChatClient chatClient;

	public AIModerationServiceImpl(ChatModel chatModel) {
		this.chatClient = ChatClient.create(chatModel);
	}

	@Override
	public boolean isAllowed(String text) {
		try {
			String promptText = "啟用文字審核功能...如果內文無異常回覆'允許'如果內文不合適回覆'屏蔽'。回覆:" + text;
			String result = chatClient.prompt().user(promptText).call().content();
			return !result.trim().equalsIgnoreCase("屏蔽");
		} catch (Exception e) {
			System.err.println("AI連接異常: " + e.getMessage());
			return true;
		}
	}

}