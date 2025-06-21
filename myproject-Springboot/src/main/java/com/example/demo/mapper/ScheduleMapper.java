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
		ScheduleDto dto = new ScheduleDto();
		dto.setScheduleId(schedule.getScheduleId() != null ? schedule.getScheduleId().intValue() : null);
		dto.setWorkDate(schedule.getWorkDate());
		dto.setShiftType(schedule.getShiftType() != null? schedule.getShiftType().name() : null);
		dto.setCreateTime(schedule.getCreateTime());
		dto.setUpdateTime(schedule.getUpdateTime());
		if(schedule.getWorkUser() != null) {
			dto.setUserId(schedule.getWorkUser().getUserId());
			dto.setAccountId(schedule.getWorkUser().getAccountId());
			dto.setUsername(schedule.getWorkUser().getUsername());
		}
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
