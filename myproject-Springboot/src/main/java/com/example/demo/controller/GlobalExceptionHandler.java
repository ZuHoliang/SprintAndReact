package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiResponse<Void>> handResponseEntity(ResponseStatusException ex){
		String msg = ex.getReason();
		if (msg == null || msg.isEmpty()) {
			msg = ex.getStatusCode().toString();
		}
		ApiResponse<Void> body = ApiResponse.error(ex.getStatusCode().value(), msg);
		return ResponseEntity.status(ex.getStatusCode()).body(body);
				
	}

}
