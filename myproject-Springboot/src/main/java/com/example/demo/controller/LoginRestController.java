package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.AccountDisabledException;
import com.example.demo.exception.CertException;
import com.example.demo.exception.PasswordInvalidException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.dto.UserCert;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.CertService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8008" }, allowCredentials = "true")
public class LoginRestController {

	@Autowired
	private CertService certService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserCert>> login(@RequestParam String accountId, @RequestParam String password,
			@RequestParam(defaultValue = "false") boolean rememberMe, HttpSession session) {
		try {
			UserCert cert = certService.getCert(accountId, password);
			session.setAttribute("userCert", cert);
			if (rememberMe) {
				session.setMaxInactiveInterval(60*60*24*7);
			}
			return ResponseEntity.ok(ApiResponse.success("登入成功", cert));
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error(401, "帳號錯誤", "LOGIN_ACCOUNT_NOT_FOUND"));			
		} catch (PasswordInvalidException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error(401, "密碼錯誤","LOGIN_PASSWORD_INVALID"));
		} catch (AccountDisabledException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(ApiResponse.error(403, "帳號已停用", "LOGIN_ACCOUNT_DISABLED"));
		} catch (CertException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.error(500,"登入失敗","LOGIN_UNKNOWN_ERROR"));
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
		if (session.getAttribute("userCert") == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(401, "登出失敗:未登入"));
		}
		session.invalidate();
		return ResponseEntity.ok(ApiResponse.success("登出成功", null));
	}

	// 檢查登入狀態
	@GetMapping("/check")
	public ResponseEntity<ApiResponse<UserCert>> checkLogin(HttpSession session) {
		UserCert userCert = (UserCert) session.getAttribute("userCert");
		if (userCert != null) {
			return ResponseEntity.ok(ApiResponse.success("已登入", userCert));
		} else {
			//(未登入放行，但不發放Cert)
			return ResponseEntity.ok(ApiResponse.success("未登入", null));
			//return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(401, "未登入"));
		}
	}

}
