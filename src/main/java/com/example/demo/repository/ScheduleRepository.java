package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Schedule;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.ShiftType;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	// 查詢某天排班(每個排班最多2人)
	List<Schedule> findByWorkDateAndShiftType(LocalDate workDate, ShiftType shiftType);

	// 查詢某位員工的所有排班
	List<Schedule> findByWorkUser(User user);
	
	//用來排班人數是否大於2
	int countByWorkDateAndShiftType(LocalDate workDate, ShiftType shiftType);
	
	//檢查是否重複排班
	boolean existsByWorkUserAndWorkDateAndShiftType(User user, LocalDate workDate, ShiftType shiftType);

	// 查詢某位員工在某天是否已排班
	void deleteByWorkUserAndWorkDateAndShiftType(User user, LocalDate workDate, ShiftType shiftType);

	// 查詢員工當日的班別
	Schedule findByWorkUserAndWorkDate(User user, LocalDate workDate);
	
	// 查詢員工本月排班數量
	@Query("SELECT COUNT(s) FROM Schedule s WHERE s.workUser = :user AND MONTH(s.workDate) = :month AND YEAR(s.workDate) = :year")
	int contByUserAndMonth(@Param("user") User user, @Param("month") int month, @Param("year") int year);	
	
	// 查詢單個員工當月的排班
	@Query("SELECT s FROM Schedule s WHERE s.workUser = :user AND MONTH(s.workDate) =:month AND YEAR(s.workDate) =:year")
	List<Schedule> findThisMonthSchedules(@Param("user") User user, @Param("month") int month, @Param("year") int year);
	
	// 查詢員工當月剩餘排班
	@Query("SELECT s FROM Schedule s WHERE s.workUser = :user AND s.workDate BETWEEN :from AND :to")
	List<Schedule> findUpcomingSchedules(@Param("user") User user, @Param("from") LocalDate from, @Param("to") LocalDate to);
	
	// 查詢指定月份全部排班
    @Query("SELECT s FROM Schedule s WHERE MONTH(s.workDate) = :month AND YEAR(s.workDate) = :year")
    List<Schedule> findByMonth(@Param("year") int year, @Param("month") int month);
	
}
