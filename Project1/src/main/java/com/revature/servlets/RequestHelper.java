package com.revature.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.controllers.EmployeeHomeController;
import com.revature.controllers.LoginController;
import com.revature.controllers.LogoutController;
import com.revature.controllers.ManagerHomeController;
import com.revature.json.ValidLoginController;
import com.revature.jsoncontrollers.AllReimbsController;
import com.revature.jsoncontrollers.ApproveController;
import com.revature.jsoncontrollers.CreateReimbController;
import com.revature.jsoncontrollers.CreateUserController;
import com.revature.jsoncontrollers.DenyController;
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
		case "/Project1--ReimbSys/mhome.rsys":
			System.out.println("Switching to home");
			return ManagerHomeController.display(req);
		case "/Project1--ReimbSys/logout.rsys":
			System.out.println("Switching to mainpage");
			return LogoutController.logout(req);
		default:
			return LoginController.login(req);
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
		case "/Project1--ReimbSys/createreimb.json":
			CreateReimbController.createReimb(req, res);
			break;
		case "/Project1--ReimbSys/getreimbs.json":
			AllReimbsController.getReimbs(req, res);
			break;
		case "/Project1--ReimbSys/updateapprove.json":
			ApproveController.approve(req, res);
			break;
		case "/Project1--ReimbSys/udpatedeny.json":
			DenyController.deny(req, res);
			break;
		case "/Project1--ReimbSys/validlogin.json":
			ValidLoginController.check(req, res);
			break;
		default:
			LoginController.login(req);
		}
	}
}
