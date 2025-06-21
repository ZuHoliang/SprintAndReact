package com.example.demo.filter;

import java.io.IOException;

import com.example.demo.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/api/*" }) // 需要登入才能訪問的網頁
public class LoginRestFilter extends HttpFilter {

	private boolean isPublicEndpoint(HttpServletRequest request) {
		//預設通過
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
		
		String path = request.getRequestURI();
		// 移除路徑部屬
		String context = request.getContextPath();
		if (context != null && !context.isEmpty() && path.startsWith(context)) {
			path = path.substring(context.length());
		}
		// 首頁放行
		if (path.startsWith("/api/home/")) {
			return true;
		}
		// 公告放行
		if (path.startsWith("/api/announcements") && "GET".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		// 認證相關接口放行
		if (path.startsWith("/api/auth")) {
			return true;
		}
		return false;
	}

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			chain.doFilter(request, response);
			return;
		}
		if (isPublicEndpoint(request)) {
			chain.doFilter(request, response);
			return;
		}

		// 檢查是否登入
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userCert") == null) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");

			ApiResponse<?> apiResponse = ApiResponse.error(401, "請先登入");

			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(apiResponse);
			response.getWriter().write(json);
			return;
		}

		// 有userCert則通過
		chain.doFilter(request, response);
	}

}