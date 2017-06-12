package com.sebatmedikal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sebatmedikal.domain.Login;
import com.sebatmedikal.domain.Role;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.repository.UserRepository;
import com.sebatmedikal.util.ImageUtil;
import com.sebatmedikal.util.LogUtil;
import com.sebatmedikal.util.NullUtil;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private LoginService loginService;

	public void save(User user) {
		save(user, null);
	}

	public void save(User user, MultipartFile originalImage) {

		if (originalImage != null) {
			if (!originalImage.isEmpty()) {
				try {
					byte[] bytes = originalImage.getBytes();
					byte[] image = ImageUtil.resize(bytes);
					user.setImage(image);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		userRepository.save(user);
	}

	public User findByUsername(String username) {
		LogUtil.logMessage(getClass(), "findByUsername");
		return userRepository.findByUsername(username);
	}

	public User findById(long id) {
		return userRepository.findOne(id);
	}

	public List<User> findByRole(Role role) {
		return userRepository.findByRole(role);
	}

	public List<User> findByRoleId(long id) {
		Role role = roleService.findById(id);
		return userRepository.findByRole(role);
	}

	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	public void delete(long id) {
		userRepository.delete(id);
	}

	public User login(String username, String password, String fcmRegistrationId) {
		User user = userRepository.findByUsernameAndPassword(username, password);
		if (NullUtil.isNull(user)) {
			return user;
		}

		Login zombieLogin = loginService.findLogin(username);
		if (NullUtil.isNotNull(zombieLogin)) {
			zombieLogin.setEndDate(new Date());
			loginService.save(zombieLogin);

			user.setLastLoginDate(zombieLogin.getBeginDate());
			save(user);
		}

		Login login = new Login();
		login.setUsername(username);
		login.setBeginDate(new Date());
		loginService.save(login);

		user.setFcmRegistrationId(fcmRegistrationId);
		user.setOnline(true);
		userRepository.save(user);

		return user;
	}

	public boolean logout(String username) {
		Login login = loginService.findLogin(username);
		if (NullUtil.isNull(login)) {
			LogUtil.logMessage(getClass(), "User is already logout");
			return false;
		}

		login.setEndDate(new Date());
		loginService.save(login);

		User user = userRepository.findByUsername(username);
		user.setLastLoginDate(login.getBeginDate());
		user.setOnline(false);
		save(user);

		return true;
	}

	public boolean changePassword(String username, String password, String newPassword) {
		User user = userRepository.findByUsernameAndPassword(username, password);
		if (NullUtil.isNull(user)) {
			return false;
		}

		user.setPassword(newPassword);
		save(user);

		return true;
	}

	public List<Login> getLoginUsers() {
		return loginService.findOnlineAll();
	}

	public boolean isUserExist(User user) {
		if (findByUsername(user.getUsername()) != null) {
			return true;
		}

		return false;
	}

	public User getCreatedBy(User user) {
		return findByUsername(user.getCreatedBy());
	}

	public ArrayList<String> findAllFcmRegistrationIds() {
		List<User> users = userRepository.findAll();
		ArrayList<String> fcmRegistrationIds = new ArrayList<>();

		for (User user : users) {
			if (NullUtil.isNotNull(user.getFcmRegistrationId())) {
				fcmRegistrationIds.add(user.getFcmRegistrationId());
			}
		}

		return fcmRegistrationIds;
	}

	public String findFcmRegistrationIdByUsername(String username) {
		User user = userRepository.findByUsername(username);
		return user.getFcmRegistrationId();
	}

	public String findFcmRegistrationIdById(long id) {
		User user = userRepository.findOne(id);
		return user.getFcmRegistrationId();
	}
}
