package com.revature.jsoncontrollers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.User;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;

public class UserController {

	public static void userFinder(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		Integer userId = (Integer) req.getSession().getAttribute("loggedId");
		
		UserDao ud = new UserDaoDB();
		
		
		res.setContentType("application/json");
		
		User user = ud.getUser(userId);
		String validjson = new ObjectMapper().writeValueAsString(user);
		System.out.println(validjson);
		res.getWriter().write(validjson);
	}
}
