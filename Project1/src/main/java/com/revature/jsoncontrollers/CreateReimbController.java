package com.revature.jsoncontrollers;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.beans.Reimbursement;
import com.revature.beans.Reimbursement.ReimbStatus;
import com.revature.beans.Reimbursement.ReimbType;
import com.revature.beans.User;
import com.revature.dao.ReimbursementDaoDB;
import com.revature.dao.ReimbursmentDao;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;
import com.revature.services.ReimbursementService;

public class CreateReimbController {

	public static void createReimb(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		if (!req.getMethod().equals("POST")) {
			System.out.println("returning home because of POST method");
			res.sendRedirect("resources/html/index.html");
		}
		
		Integer userId = (Integer) req.getSession().getAttribute("loggedId");

		UserDao udao =  new UserDaoDB();
		ReimbursmentDao rdao =  new ReimbursementDaoDB();
		ReimbursementService rs = new ReimbursementService(rdao);
		

		System.out.println("in create reimb");
		Double amount = Double.parseDouble(req.getParameter("amount"));
		LocalDateTime submitted = LocalDateTime.now().withNano(0);
		String description = req.getParameter("description");
		User author = udao.getUser(userId);
		User resolver = udao.getUser(2);
		System.out.println(req.getParameter("select"));

		Integer type = Integer.parseInt(req.getParameter("select"));
		ReimbType rt;
		
		switch(type) {
		case 1:
			rt = ReimbType.LODGING;
			break;
		case 2:
			rt = ReimbType.TRAVEL;
			break;
		case 3:
			rt = ReimbType.FOOD;
			break;
		default:
			rt = ReimbType.OTHER;
		}
		
		Reimbursement r = new Reimbursement(amount, submitted, description, author, resolver, ReimbStatus.PENDING, rt);
		System.out.println("About to create: " + r);
		rdao.addReimbursment(r);
		
		res.sendRedirect("http://localhost:8080/Project1--ReimbSys/ehome.rsys");
		
	}
}
