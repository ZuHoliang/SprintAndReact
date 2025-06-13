package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.ShiftSwapRequest;
import com.example.demo.model.entity.User;

@Repository
public interface ShiftSwapRequestRepository extends JpaRepository<ShiftSwapRequest, Long>{
	
	//查詢某位使用者發出的換班請求
	List<ShiftSwapRequest> findByRequestUser(User requestUser);
	
	//查詢某位使用者收到的換班請求
	List<ShiftSwapRequest> findByTargetUser(User targetUser);
	
	//查詢某天的所有排班
    List<ShiftSwapRequest> findBySwapDate(LocalDate swapDate);
    
    // 查詢某天某人發出/收到的換班申請（避免重複）
    List<ShiftSwapRequest> findByRequestUserAndSwapDate(User requestUser, LocalDate swapDate);
    List<ShiftSwapRequest> findByTargetUserAndSwapDate(User targetUser, LocalDate swapDate);
    
    //查詢整月的換班請求
    @Query("SELECT r FROM ShiftSwapRequest r WHERE MONTH(r.swapDate) = :month AND YEAR(r.swapDate) = :year")
    List<ShiftSwapRequest> findRequestsByMonth(@Param("month") int month, @Param("year") int year);

}
