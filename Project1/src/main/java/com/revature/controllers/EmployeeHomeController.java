package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

public class EmployeeHomeController {

	public static String display(HttpServletRequest req) {
		System.out.println(req.getRequestURI());
		return "resources/html/ehome.html";
	}
}
