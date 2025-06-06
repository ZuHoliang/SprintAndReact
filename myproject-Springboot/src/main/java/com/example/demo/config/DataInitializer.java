package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.demo.service.UserService;

//建立初始化測試帳號
@Component
public class DataInitializer implements CommandLineRunner {
	
	private final UserService userService;
	
	public DataInitializer(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public void run(String... args) throws Exception {
		if(userService.getUser("0000") == null){
			userService.addUser("測試管理者", "Admin123", 2, true);
		}
		if(userService.getUser("0001") == null) {
			userService.addUser("測試使用者", "User123", 1, true);
		}
		
		System.out.println("初始化帳號建立完成");
	}

}
