package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	@Query(value = "select user_id, account_id, username, password_hash, salt, active, role from users where account_id=:accountId", nativeQuery = true)
	User getUser(String accountId);
	
	@Query("SELECT MAX(u.accountId) FROM User u")
    String findMaxAccountId();
}
