package com.example.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementEditDto {

	@NotBlank(message = "請輸入標題")
	@Size(min = 3, message = "標題至少為3個字")
	private String title;

	@NotBlank(message = "請輸入內文")
	private String content;

	private Boolean announcementActive;
	// 作者暫不更改
}
