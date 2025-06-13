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
public class ScheduleDto {
	private Integer scheduleId;
	private Integer userId;
	private String accountId;
	private String username;
	private String shiftType;
	private LocalDate workDate;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;

}
