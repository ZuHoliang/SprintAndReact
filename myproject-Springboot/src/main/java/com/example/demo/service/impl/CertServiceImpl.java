package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AccountDisabledException;
import com.example.demo.exception.PasswordInvalidException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CertService;
import com.example.demo.util.Hash;

@Service
public class CertServiceImpl implements CertService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserCert getCert(String accountId, String password)
			throws UserNotFoundException, PasswordInvalidException, AccountDisabledException {
		// 檢查帳號
		User user = userRepository.getUser(accountId);
		if (user == null) {
			throw new UserNotFoundException("帳號錯誤");
		}
		// 2. 密碼 hash 比對
		String passwordHash = Hash.getPasswordHash(password, user.getSalt());
		if (!passwordHash.equals(user.getPasswordHash())) {
			throw new PasswordInvalidException("密碼錯誤");
		}

		// 3.檢查帳號是否啟用
		if (user.getActive() != null && !user.getActive()) {
			throw new AccountDisabledException("帳號已停用");
		}

		// 4. 簽發憑證
		// 若使用者不存在，預設為 0
		int roleValue = user.getRole() != null ? user.getRole() : 0;

		return new UserCert(user.getUserId(), user.getAccountId(), user.getUsername(), roleValue);
	}

}
