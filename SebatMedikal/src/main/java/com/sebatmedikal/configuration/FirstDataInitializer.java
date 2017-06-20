package com.sebatmedikal.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sebatmedikal.domain.OperationType;
import com.sebatmedikal.domain.Role;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.gcm.NotificationSender;
import com.sebatmedikal.gcm.gcm.GcmPushImpl;
import com.sebatmedikal.service.OperationTypeService;
import com.sebatmedikal.service.RoleService;
import com.sebatmedikal.service.UserService;
import com.sebatmedikal.util.LogUtil;
import com.sebatmedikal.util.NullUtil;

@Component
public class FirstDataInitializer implements CommandLineRunner {
	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private OperationTypeService operationTypeService;

	@Override
	public void run(String... arg0) throws Exception {
		List<Role> roles = roleService.findAll();
		if (roles.isEmpty()) {
			roles.add(new Role("ADMIN", "ADMIN", "\\R"));
			roles.add(new Role("USER", "USER", "\\R"));
			roleService.save(roles);
			LogUtil.logMessage(this.getClass(), roles.size() + " role saved");
		}

		User user = userService.findByUsername("sebat.medikal");
		if (NullUtil.isNull(user)) {
			user = new User();
			user.setFirstName("SEBAT");
			user.setLastName("MEDIKAL");
			user.setUsername("sebat.medikal");
			user.setPassword("sebat12345");
			user.setRole(roles.get(0));
			user.setCreatedBy("\\R");

			userService.save(user);
			LogUtil.logMessage(this.getClass(), "sebat.medikal user saved");
		}

		List<OperationType> operationTypes = operationTypeService.findAll();
		if (operationTypes.isEmpty()) {
			operationTypes = new ArrayList<>();
			operationTypes.add(new OperationType("ALIM", user.getUsername(), false));
			operationTypes.add(new OperationType("SATIM", user.getUsername(), true));

			operationTypeService.save(operationTypes);
			LogUtil.logMessage(this.getClass(), operationTypes.size() + " operationType saved");
		}

		userService.logoutAll();

		GcmPushImpl gcmPushImpl = new GcmPushImpl(SecurityConfiguration.FCM_SERVER_KEY);
		NotificationSender.serverOnline(gcmPushImpl, userService.findAllFcmRegistrationIds());
	}
}