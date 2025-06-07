package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允許所有路徑的跨域請求
        .allowedOrigins("http://localhost:5173", "http://localhost:8008") // 允許來自這些來源的請求
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法，務必包含 OPTIONS
        .allowedHeaders("*") // 允許所有請求標頭
        .allowCredentials(true); // 允許發送 cookies (例如 session cookie)
//      .maxAge(3600); // 預檢請求的緩存時間 (秒)
}

}
