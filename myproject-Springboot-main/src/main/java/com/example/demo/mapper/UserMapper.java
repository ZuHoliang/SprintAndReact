package com.example.demo.mapper;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.AccountManageDto;
import com.example.demo.model.entity.User;

@Component
public class UserMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	//Entity轉DTO
	public UserDto toDto(User user) {		
		return modelMapper.map(user, UserDto.class);
	}
	
	//DTO轉Entity
	public User toEntity(UserDto userDto) {
		return modelMapper.map(userDto, User.class);
	}
	
	public void updateEntityEditDto(AccountManageDto editDto, User user) {
		if(editDto.getUsername() != null) {
			user.setUsername(editDto.getUsername());
		}
	}	

}
