package com.example.demo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")//設置端點
				.setAllowedOrigins("http://localhost:5173", "http://localhost:8008")
				.withSockJS();
	}
	
	//定義訊息路由規則
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic"); //發送以topic為開頭的目的地訊息(訊息代理的前綴)
		registry.setApplicationDestinationPrefixes("/app"); //應用程式前綴
	}

}
