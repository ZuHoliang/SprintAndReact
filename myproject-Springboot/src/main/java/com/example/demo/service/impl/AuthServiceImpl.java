package com.example.demo.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.dto.UserCert;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.HttpSession;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Override
	public void checkAdminPermission(HttpSession session) {
		UserCert cert = (UserCert) session.getAttribute("userCert");
		if(cert == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "未登入");
		}
		if(!cert.isAdmin()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,"無權限");
		}
	}
	
	@Override
	public void checkAuthenticated(HttpSession session) {
		UserCert cert = (UserCert) session.getAttribute("userCert");
		if (cert == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "未登入");
		}
	}

}
