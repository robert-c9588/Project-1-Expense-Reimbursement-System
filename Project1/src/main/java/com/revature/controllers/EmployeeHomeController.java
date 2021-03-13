package com.revature.controllers;

import javax.servlet.http.HttpServletRequest;

import com.revature.beans.User;
import com.revature.dao.ReimbursementDaoDB;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;
import com.revature.services.UserService;

public class EmployeeHomeController {

	public static String display(HttpServletRequest req) {
		
		return "resources/html/ehome.html";
	}
}
