package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long>{
	
	//所有公告(可分頁)
	List<Announcement> findAllByOrderByCreatedTimeDesc(Pageable pageable);
	
	//限制查詢公告數量
    List<Announcement> findTop5ByAnnouncementActiveTrueOrderByCreatedTimeDesc();
    
    //所有啟用公告
    List<Announcement> findByAnnouncementActiveTrueOrderByCreatedTimeDesc();    
    
}
