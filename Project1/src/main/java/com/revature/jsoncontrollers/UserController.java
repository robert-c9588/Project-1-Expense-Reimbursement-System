package com.revature.jsoncontrollers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.User;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;

public class UserController {

	public static void userFinder(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Integer userId = (Integer) req.getSession().getAttribute("loggedId");
		
		UserDao ud = new UserDaoDB();
		
		User user = ud.getUser(userId);
		res.getWriter().write(new ObjectMapper().writeValueAsString(user));
	}
}
