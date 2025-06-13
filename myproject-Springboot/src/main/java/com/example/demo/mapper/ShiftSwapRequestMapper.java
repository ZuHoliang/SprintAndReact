package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.ShiftSwapRequestDto;
import com.example.demo.model.entity.ShiftSwapRequest;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

@Component
public class ShiftSwapRequestMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	
	//Entity轉DTO
	public ShiftSwapRequestDto toDto(ShiftSwapRequest request) {
		ShiftSwapRequestDto dto = modelMapper.map(request, ShiftSwapRequestDto.class);
		
		if(request.getRequestUser() != null) {
			dto.setRequestUserId(request.getRequestUser().getUserId());
			dto.setRequestUsername(request.getRequestUser().getUsername());
		}
		
		if(request.getTargetUser() != null) {
            dto.setTargetUserId(request.getTargetUser().getUserId());
            dto.setTargetUsername(request.getTargetUser().getUsername());
    }
		
		dto.setSwapFromShift(request.getSwapFromShift() != null ? request.getSwapFromShift().name() : null);
		dto.setSwapToShift(request.getSwapToShift() != null ? request.getSwapToShift().name() : null);
		dto.setReqStatus(request.getReqStatus().name());
		
		return dto;
	}	
	
	//DTO轉Entity
	public ShiftSwapRequest toEntity(ShiftSwapRequestDto dto) {
		ShiftSwapRequest entity = modelMapper.map(dto, ShiftSwapRequest.class);
		
		if(dto.getRequestUserId() != null) {
			User requestUser = userRepository.findById(dto.getRequestUserId()).orElse(null);
			entity.setRequestUser(requestUser);
		}
		
		if(dto.getTargetUserId() != null) {
			User targetUser = userRepository.findById(dto.getTargetUserId()).orElse(null);
			entity.setTargetUser(targetUser);
		}
		
		return entity;
	}
	

}
