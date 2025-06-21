package com.example.demo.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShiftSwapRequestDto {
	private Integer shiftSwapId;
	
	private Integer requestUserId;
	private String requestUsername;
	
	private Integer targetUserId;
	private String targetUsername;
	
	private String swapFromShift;
	private String swapToShift;
	
	private String swapMessage;
	private String respMessage;
	
	private String reqStatus;
	
	private LocalDate swapDate;
	
	private LocalDateTime requestTime;
	private LocalDateTime responseTime;
	
}
