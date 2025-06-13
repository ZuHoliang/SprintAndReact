package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.ScheduleDto;
import com.example.demo.model.entity.Schedule;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

@Component
public class ScheduleMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepository;

	//Entity轉DTO
	public ScheduleDto toDto(Schedule schedule) {
		ScheduleDto dto = modelMapper.map(schedule, ScheduleDto.class);
		if (schedule.getWorkUser() != null) {
			dto.setUserId(schedule.getWorkUser().getUserId());
			dto.setUsername(schedule.getWorkUser().getUsername());
		}
		dto.setShiftType(schedule.getShiftType().name());
		return dto;
	
	}
	
	//DTO轉Entity
	public Schedule toEntity(ScheduleDto dto) {
		Schedule entity = modelMapper.map(dto, Schedule.class);
		if(dto.getUserId() != null) {
			User user = userRepository.findById(dto.getUserId()).orElse(null);
			entity.setWorkUser(user);
		}
		return entity;
	}
	
}
