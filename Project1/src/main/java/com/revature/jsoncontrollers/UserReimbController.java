package com.revature.jsoncontrollers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Reimbursement;
import com.revature.beans.User;
import com.revature.dao.UserDao;
import com.revature.dao.ReimbursementDaoDB;
import com.revature.dao.ReimbursmentDao;
import com.revature.dao.UserDaoDB;

public class UserReimbController {
	public static void uReimbFinder(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		Integer userId = (Integer) req.getSession().getAttribute("loggedId");
		
		UserDao ud = new UserDaoDB();
		ReimbursmentDao rd = new ReimbursementDaoDB();
		
		User user = ud.getUser(userId);
		
		List<Reimbursement>rlist =  rd.getReimbursementsByEmployee(user);
		
		
		
		res.getWriter().write(new ObjectMapper().writeValueAsString(rlist));
	}
}
