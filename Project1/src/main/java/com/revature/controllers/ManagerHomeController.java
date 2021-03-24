package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

public class ManagerHomeController {
	public static String display(HttpServletRequest req) {
		System.out.println(req.getRequestURI());

		return "resources/html/mhome.html";
	}
}
