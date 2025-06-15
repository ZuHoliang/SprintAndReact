package com.example.demo.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDto {

	private Long announcementId;
	private String title;
	private String content;
	private String authorName; // 通過authorId查詢Users
	private LocalDateTime createdTime;
	private LocalDateTime updateTime;
	private Boolean announcementActive;

}
