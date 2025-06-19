package com.example.demo.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.service.AIModerationService;

@Service
public class AIModerationServiceImpl implements AIModerationService {

	private final ChatClient chatClient;

	@Autowired
	public AIModerationServiceImpl(ChatModel chatModel) {
		this.chatClient = ChatClient.create(chatModel);
	}

	static boolean containsBlock(String result) {
		String normalized = result == null ? "" : result.toLowerCase();
		return normalized.contains("block");
	}

	@Override
	public boolean isAllowed(String text) {
		try {
			String promptText = "啟用文字審核功能...如果內文無異常回覆'Allow'如果內文不合適回覆'Block'。回覆:" + text;
			String result = chatClient.prompt().user(promptText).call().content();
			System.out.println(result);
			return !containsBlock(result);
		} catch (Exception e) {
			System.err.println("AI連接異常: " + e.getMessage());
			return true;
		}
	}

}