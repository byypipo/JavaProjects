package com.sebatmedikal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sebatmedikal.domain.Login;
import com.sebatmedikal.repository.LoginRepository;

@Service
public class LoginService {
	@Autowired
	private LoginRepository loginRepository;

	public void save(Login login) {
		loginRepository.save(login);
	}

	public void save(Iterable<Login> logins) {
		loginRepository.save(logins);
	}

	public Login findLogin(String username) {
		return loginRepository.findByUsernameAndEndDateIsNull(username);
	}

	public Login findLastLogin(String username) {
		return loginRepository.findLastByUsername(username);
	}

	public Login findById(long id) {
		return loginRepository.findOne(id);
	}

	public List<Login> findOnlineAll() {
		return loginRepository.readAllByEndDateNull();
	}
}
