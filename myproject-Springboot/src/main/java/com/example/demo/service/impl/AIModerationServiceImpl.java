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
         if (result == null) {
                 return false;
         }
         String[] lines = result.trim().split("\\R"); // \R 支援跨平台換行
         String lastLine = lines[lines.length - 1].trim();
         lastLine = lastLine.replaceAll("[\\p{Punct}]+$", ""); // 去尾標點
         return lastLine.equalsIgnoreCase("block");
 }

	@Override
	public boolean isAllowed(String text) {
		try {
			String promptText = "啟用文字審核功能...快速判斷內文是否合適，如果無異常回覆'allow'如果內文不合適回覆'block'。回覆:" + text;
			String result = chatClient.prompt().user(promptText).call().content();
			System.out.println(result);
			return !containsBlock(result);
		} catch (Exception e) {
			System.err.println("AI連接異常: " + e.getMessage());
			return true;
		}
	}

}