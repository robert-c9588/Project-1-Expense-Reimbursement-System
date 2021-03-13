package com.revature.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.revature.jsoncontrollers.UserController;
import com.revature.jsoncontrollers.UserReimbController;

public class JSONRequestHelper extends HttpServlet{

	public static String process(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {

		System.out.println(req.getRequestURI());

		switch (req.getRequestURI()) {
		case "/Project1--ReimbSys/user.json":
			return UserController.userFinder(req, res);
			break;
		case "/Project1--ReimbSys/ureimb.json":
			UserReimbController.uReimbFinder(req, res);
			break;
			
		default:
			

		}
	
	
	}
}
