package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.ShiftSwapRequestDto;
import com.example.demo.model.entity.ShiftSwapRequest;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.RequestStatus;
import com.example.demo.model.enums.ShiftType;
import com.example.demo.repository.UserRepository;

@Component
public class ShiftSwapRequestMapper {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	// Entity轉DTO
	public ShiftSwapRequestDto toDto(ShiftSwapRequest request) {
		if (request == null) {
			return null;
		}
		ShiftSwapRequestDto dto = new ShiftSwapRequestDto();
		dto.setShiftSwapId(request.getShiftSwapId() != null ? request.getShiftSwapId().intValue() : null);

		if (request.getRequestUser() != null) {
			dto.setRequestUserId(request.getRequestUser().getUserId());
			dto.setRequestUsername(request.getRequestUser().getUsername());
		}

		if (request.getTargetUser() != null) {
			dto.setTargetUserId(request.getTargetUser().getUserId());
			dto.setTargetUsername(request.getTargetUser().getUsername());
		}

		dto.setSwapFromShift(request.getSwapFromShift() != null ? request.getSwapFromShift().name() : null);
		dto.setSwapToShift(request.getSwapToShift() != null ? request.getSwapToShift().name() : null);
		dto.setSwapMessage(request.getSwapMessage());
		dto.setRespMessage(request.getRespMessage());
		dto.setReqStatus(request.getReqStatus() != null ? request.getReqStatus().name() : null);
		dto.setSwapDate(request.getSwapDate());
		dto.setRequestTime(request.getRequestTime());
		dto.setResponseTime(request.getResponseTime());

		return dto;
	}

	// DTO轉Entity
	public ShiftSwapRequest toEntity(ShiftSwapRequestDto dto) {
		if (dto == null) {
			return null;
		}
		ShiftSwapRequest entity = new ShiftSwapRequest();
		entity.setShiftSwapId(dto.getShiftSwapId() != null ? dto.getShiftSwapId().longValue() : null);

		if (dto.getRequestUserId() != null) {
			User requestUser = userRepository.findById(dto.getRequestUserId()).orElse(null);
			entity.setRequestUser(requestUser);
		}

		if (dto.getTargetUserId() != null) {
			User targetUser = userRepository.findById(dto.getTargetUserId()).orElse(null);
			entity.setTargetUser(targetUser);
		}

		entity.setSwapDate(dto.getSwapDate());
		entity.setSwapToShift(dto.getSwapToShift() != null ? ShiftType.valueOf(dto.getSwapToShift()) : null);
		entity.setSwapFromShift(dto.getSwapFromShift() != null ? ShiftType.valueOf(dto.getSwapFromShift()) : null);
		entity.setSwapMessage(dto.getSwapMessage());
		entity.setRespMessage(dto.getRespMessage());
		entity.setReqStatus(dto.getReqStatus() != null ? RequestStatus.valueOf(dto.getReqStatus()) : null);
		entity.setRequestTime(dto.getRequestTime());
		entity.setResponseTime(dto.getResponseTime());

		return entity;
	}
}
