package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

	// 所有公告(可分頁)
	List<Announcement> findAllByOrderByCreatedTimeDesc(Pageable pageable);

	// 限制查詢公告數量
	List<Announcement> findTop5ByAnnouncementActiveTrueOrderByCreatedTimeDesc();

	// 所有啟用公告
	List<Announcement> findByAnnouncementActiveTrueOrderByCreatedTimeDesc();
	
	//查詢公告
	@Query("SELECT a FROM Announcement a WHERE a.announcementActive = true "
			+ "AND(:keyword IS NULL OR a.title LIKE CONCAT ('%', :keyword, '%')) "
			+ "AND(:startDate IS NULL OR a.createdTime >= :startDate) " 
			+ "AND(:endDate IS NULL OR a.createdTime <= :endDate) "
			+ "ORDER BY a.createdTime DESC")
	List<Announcement> searchByKeyword(
			@Param("keyword")String keyword,
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate")LocalDateTime endDateTime);
}
