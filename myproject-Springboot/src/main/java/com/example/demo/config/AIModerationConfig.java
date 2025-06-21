package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class AIModerationConfig {
	private boolean usernameCheck = true;
	private boolean messageCheck = true;
	private boolean announcementCheck = true;
	
	public boolean isUsernameCheck() {
		return usernameCheck;
	}
	
	public void setUsernameCheck(boolean usernameCheck) {
		this.usernameCheck = usernameCheck;
	}
	
	public boolean isMessageCheck() {
		return messageCheck;
	}
	
	public void setMessageCheck(boolean messageCheck) {
		this.messageCheck = messageCheck;
	}
	
	public boolean isAnnouncementCheck() {
		return announcementCheck;
	}
	
	public void setAnnouncementCheck(boolean announcementCheck) {
		this.announcementCheck = announcementCheck;
	}

}
