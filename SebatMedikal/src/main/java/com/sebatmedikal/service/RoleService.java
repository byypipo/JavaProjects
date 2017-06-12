package com.sebatmedikal.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sebatmedikal.domain.Role;
import com.sebatmedikal.domain.User;
import com.sebatmedikal.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	public void save(Role role) {
		roleRepository.save(role);
	}

	public void save(Iterable<Role> roles) {
		roleRepository.save(roles);
	}

	public Role findById(long id) {
		return roleRepository.findOne(id);
	}

	public List<Role> findAll() {
		return (List<Role>) roleRepository.findAll();
	}

	public void delete(long id) {
		roleRepository.delete(id);
	}

	public List<String> getUserTypeNames() {
		List<String> roles = new ArrayList<String>();
		Iterator<Role> iterator = findAll().iterator();

		while (iterator.hasNext()) {
			Role userType = (Role) iterator.next();
			roles.add(userType.getRoleName());
		}

		return roles;
	}

	public User getCreatedBy(Role role) {
		return userService.findByUsername(role.getCreatedBy());
	}
}
