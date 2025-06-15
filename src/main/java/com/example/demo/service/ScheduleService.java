package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.model.entity.Schedule;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.ShiftType;

public interface ScheduleService {
	
	//檢查排班是否已滿(最多2人)
	boolean isShiftFull(LocalDate date, ShiftType shiftType);

	//檢查使否重複排班
	boolean hasUserAlreadyScheduled(User user, LocalDate date, ShiftType shiftType);

	//排班
	Schedule assignShift(User user, LocalDate date, ShiftType shiftType);

	//取消排班
	void cancelShift(User user, LocalDate date, ShiftType shiftType);

	//取得整月排班
	List<Schedule> getUserMonthlySchedules(User user, int month, int year);

	//取得剩餘排班
	List<Schedule> getUserRemainingSchedules(User user, LocalDate fromDate, LocalDate toDate);

	//查詢整月全部排班
	List<Schedule> getSchedulesByDateAndShift(LocalDate date, ShiftType shiftType);
	
	//查詢指定月份全部排班(所有員工)
    List<Schedule> getMonthSchedules(int year, int month);
	
//	//查詢所有排班(暫時不用)
//	List<Schedule> getSchedulesByUser(User user);
}
