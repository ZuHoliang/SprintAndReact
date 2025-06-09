package com.example.demo.service;

import jakarta.servlet.http.HttpSession;

public interface AuthService {

	void checkAdminPermission(HttpSession session);

	void checkAuthenticated(HttpSession session);

}
