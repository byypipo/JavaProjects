package com.sebatmedikal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sebatmedikal.domain.User;
import com.sebatmedikal.model.LoginModel;
import com.sebatmedikal.service.RoleService;
import com.sebatmedikal.service.UserService;
import com.sebatmedikal.util.LogUtil;
import com.sebatmedikal.util.NullUtil;

@Controller
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView getRegisterPage() {
		LogUtil.logMessage(this.getClass(), "getRegisterPage called");
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("roles", roleService.findAll());
		model.put("user", new User());
		return new ModelAndView("register", model);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String handleRegisterForm(@Valid @ModelAttribute("user") User user, @RequestParam("file") MultipartFile file, BindingResult bindingResult, HttpServletResponse response) {

		try {
			LogUtil.logMessage(this.getClass(), "handleRegisterForm called");
			if (bindingResult.hasErrors()) {
				LogUtil.logMessage(this.getClass(), "handleRegisterForm errors");
				return "redirect:/users/register";
			}

			user.setCreatedBy("sebat.medikal");
			userService.save(user, file);
			LogUtil.logMessage(this.getClass(), "SUCCESFULLY");

		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/users/register";
		}

		return "redirect:/usersRest/" + user.getId() + "/image";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView getLoginPage() {
		LogUtil.logMessage(this.getClass(), "getLoginPage called");
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("loginModel", new LoginModel());
		return new ModelAndView("login", model);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String handleLoginForm(@Valid @ModelAttribute("loginModel") LoginModel loginModel, BindingResult bindingResult, HttpServletResponse response) {
		LogUtil.logMessage(this.getClass(), "handleLoginForm called");
		if (bindingResult.hasErrors()) {
			LogUtil.logMessage(this.getClass(), "handleLoginForm errors");
			return "redirect:/users/register";
		}

		if (loginModel == null) {
			return "redirect:/users/login";
		}

		User user = userService.login(loginModel.getUsername(), loginModel.getPassword(), null);
		if (NullUtil.isNull(user)) {
			return "redirect:/users/login";
		}

		LogUtil.logMessage(this.getClass(), "LOGIN SUCCESFULLY");
		return "redirect:/usersRest/" + user.getId() + "/image";
	}
}
