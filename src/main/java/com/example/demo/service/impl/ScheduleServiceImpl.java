package com.example.demo.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.DuplicateScheduleException;
import com.example.demo.exception.ScheduleFullException;
import com.example.demo.mapper.ScheduleMapper;
import com.example.demo.model.entity.Schedule;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.ShiftType;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.service.ScheduleService;

import jakarta.transaction.Transactional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private ScheduleMapper scheduleMapper;

	// 檢查排班是否已滿(最多2人)
	@Override
	public boolean isShiftFull(LocalDate date, ShiftType shiftType) {
		int count = scheduleRepository.countByWorkDateAndShiftType(date, shiftType);
		return count >= 2;
	}

	// 檢查是否重複排班
	@Override
	public boolean hasUserAlreadyScheduled(User user, LocalDate date, ShiftType shiftType) {
		return scheduleRepository.existsByWorkUserAndWorkDateAndShiftType(user, date, shiftType);
	}

	// 排班
	@Override
	public Schedule assignShift(User user, LocalDate date, ShiftType shiftType) {
		if (isShiftFull(date, shiftType)) {
			throw new ScheduleFullException("排班已滿");
		}
		if (hasUserAlreadyScheduled(user, date, shiftType)) {
			throw new DuplicateScheduleException("重複排班");
		}

		Schedule schedule = new Schedule();
		schedule.setWorkUser(user);
		schedule.setWorkDate(date);
		schedule.setShiftType(shiftType);

		return scheduleRepository.save(schedule);
	}

	// 取消排班
	@Override
	@Transactional
	public void cancelShift(User user, LocalDate date, ShiftType shiftType) {
		scheduleRepository.deleteByWorkUserAndWorkDateAndShiftType(user, date, shiftType);
	}

	// 取得整月排班
	@Override
	public List<Schedule> getUserMonthlySchedules(User user, int month, int year) {
		return scheduleRepository.findThisMonthSchedules(user, month, year);
	}

	// 取得剩餘排班
	@Override
	public List<Schedule> getUserRemainingSchedules(User user, LocalDate fromDate, LocalDate toDate) {
		return scheduleRepository.findUpcomingSchedules(user, fromDate, toDate);
	}

	// 查詢整月全部排班
	@Override
	public List<Schedule> getSchedulesByDateAndShift(LocalDate date, ShiftType shiftType) {
		return scheduleRepository.findByWorkDateAndShiftType(date, shiftType);

	}

	// 查詢指定月份全部排班(所有員工)
	@Override
	public List<Schedule> getMonthSchedules(int year, int month) {
		return scheduleRepository.findByMonth(year, month);
	}

}
