package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserCert {
	// ID
	private Integer userId;
	// 帳號
	private String accountId;
	// 使用者名稱
	private String username; // 名稱

	private Integer role; // 權限 0:未登入 1:USER 2:ADMIN

	public boolean isAdmin() {
		return role != null && role == 2;
	}

	public boolean isUser() {
		return role == 1 || role == 2;
	}

}
