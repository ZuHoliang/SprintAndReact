package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity // (建立資料表)
@Table(name = "users") // 資料表命名
public class User {

	@Id // 主鍵、員工編號
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;
	
	//帳號
	@Column(name = "account_id", unique = true, nullable = false)
	private String accountId;
	//使用者姓名
	@Column(name = "username", unique = false, nullable = false)
	private String username;
	//密碼
	@Column(name = "password_hash", unique = false, nullable = false)
	private String passwordHash;

	@Column(name = "salt", unique = false, nullable = false)
	private String salt;

	@Column(name = "role")
	private Integer role;

	@Column(name = "active")
	private Boolean active;

//	@Column(name = "email", nullable = true)
//	private String email;

}
