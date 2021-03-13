package com.revature.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.revature.controllers.EmployeeHomeController;
import com.revature.controllers.LoginController;
import com.revature.jsoncontrollers.UserController;

public class RequestHelper extends HttpServlet{
	public static String process(HttpServletRequest req) {
		System.out.println("Processing request " + req.getRequestURI());
		
		switch(req.getRequestURI()) {
		case "/Project1--ReimbSys/login.rsys":
			System.out.println("Switching to login");
			return LoginController.login(req);
			
		case "/Project1--ReimbSys/ehome.rsys":
			System.out.println("Switching to home");
			return EmployeeHomeController.display(req);
		default:
			return "resources/html/index.html";
		}
	}
}
