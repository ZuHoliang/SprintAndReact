package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Notification;
import com.example.demo.model.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{
	List<Notification> findByRecipientOrderByCreatedTimeDesc(User recipient);

}
