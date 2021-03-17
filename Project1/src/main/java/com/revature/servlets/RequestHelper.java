package com.revature.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.revature.controllers.EmployeeHomeController;
import com.revature.controllers.LoginController;
import com.revature.jsoncontrollers.CreateUserController;
import com.revature.jsoncontrollers.UserController;
import com.revature.jsoncontrollers.UserReimbController;

public class RequestHelper extends HttpServlet {
	public static String process(HttpServletRequest req) throws ServletException, IOException {
		System.out.println("Processing request " + req.getRequestURI());

		switch (req.getRequestURI()) {
		case "/Project1--ReimbSys/login.rsys":
			System.out.println("Switching to login");
			return LoginController.login(req);

		case "/Project1--ReimbSys/ehome.rsys":
			System.out.println("Switching to home");
			return EmployeeHomeController.display(req);

		default:
			return "/resource/html/index.html";
		}
	}

	public static void process(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		System.out.println("Processing request " + req.getRequestURI());

		switch (req.getRequestURI()) {
		case "/Project1--ReimbSys/user.json":
			UserController.userFinder(req, res);
			break;
		case "/Project1--ReimbSys/ureimb.json":
			UserReimbController.uReimbFinder(req, res);
			break;
		case "/Project1--ReimbSys/createuser.json":
			CreateUserController.createUser(req, res);
			break;
		default:
			LoginController.login(req);
		}
	}
}
