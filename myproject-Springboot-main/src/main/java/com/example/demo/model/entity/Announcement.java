package com.example.demo.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "announcements")
public class Announcement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long announcementId;
	
	@Column(nullable = false)
	private String title;
	
	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;
	
	@Column(nullable = false)
	private LocalDateTime createdTime;
	
	@Column(nullable = false)
	private LocalDateTime updateTime;
	
	@Column(name = "author_id", nullable = true)//可讓系統自動發布
	private Integer authorId;
	
	@Column(name = "announcement_active")
	private Boolean announcementActive;
	
	@PrePersist
	protected void onCreate() {
		createdTime = LocalDateTime.now();
		updateTime = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updateTime = LocalDateTime.now();
	}
	

}
