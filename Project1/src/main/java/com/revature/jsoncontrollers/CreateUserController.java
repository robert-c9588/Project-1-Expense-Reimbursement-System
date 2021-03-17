package com.revature.jsoncontrollers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.User;
import com.revature.dao.ReimbursementDaoDB;
import com.revature.dao.ReimbursmentDao;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;
import com.revature.services.UserService;

public class CreateUserController {

	public static void createUser(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		if (!req.getMethod().equals("POST")) {
			System.out.println("returning home because of POST method");
			res.sendRedirect("resources/html/index.html");
		}
		
		//Get fields
		System.out.println("in create user control");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String email = req.getParameter("email");

		User u = new User(username, password, firstname, lastname, email);
		System.out.println(u);
		UserDao ud =  new  UserDaoDB();
		ReimbursmentDao rd = new ReimbursementDaoDB();
		
		UserService us = new UserService(ud, rd);
		
		boolean registered = us.register(u);
		res.getWriter().write(new ObjectMapper().writeValueAsString(registered));
		res.sendRedirect("resources/html/index.html");
	}
	
}
