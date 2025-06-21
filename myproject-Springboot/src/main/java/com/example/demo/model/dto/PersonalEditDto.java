package com.example.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonalEditDto {
	@NotBlank(message = "請輸入使用者名稱")
	@Size(min = 2, message = "使用者名稱至少為2個字")
	private String username;

//	@NotBlank(message = "請輸入密碼")
//	@Size(min = 3, message = "密碼至少為3個字")
//	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "密碼請包含大小寫與數字")
	private String password;

}
