package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

import com.revature.beans.User;
import com.revature.beans.User.UserRole;
import com.revature.dao.ReimbursementDaoDB;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;
import com.revature.services.UserService;

public class LoginController {

	public static String login(HttpServletRequest req) {
		if (!req.getMethod().equals("POST")) {
			req.getSession().setAttribute("loggedUsername", null);
			req.getSession().setAttribute("loggedPassword", null);
			req.getSession().setAttribute("loggedId", null);
			System.out.println("returning home because of POST method");
			return "resources/html/index.html";
		}
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		UserDao ud = new UserDaoDB();
		ReimbursementDaoDB rd = new ReimbursementDaoDB();

		User user = null;
		UserService us = new UserService(ud, rd);
		user = us.login(username, password);

		if (user == null) {
			return "invalid.rsys";
		} else {
			req.getSession().setAttribute("loggedUsername", username);
			req.getSession().setAttribute("loggedPassword", password);
			req.getSession().setAttribute("loggedId", user.getId());
			if (user.getRoleId().equals(UserRole.EMPLOYEE)) {
				return "ehome.rsys";
			} else {
				return "mhome.rsys";
			}
		}
	}
}
