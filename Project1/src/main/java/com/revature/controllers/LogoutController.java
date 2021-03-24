package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

public class LogoutController {
	public static String logout(HttpServletRequest req) {
		req.getSession().setAttribute("loggedIn", 0);
		req.getSession().invalidate();
		
		return "login.rsys";
	}
}
