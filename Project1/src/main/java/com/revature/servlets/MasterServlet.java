package com.revature.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
 * bit of code you might need if you are getting the no driver found exception with 
 * sql and your servlets Class.forName("org.postgresql.Driver")
 */

public class MasterServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		System.out.println("in doGet");
		req.getRequestDispatcher(RequestHelper.process(req)).forward(req, res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		System.out.println("in doPost");
		req.getRequestDispatcher(RequestHelper.process(req)).forward(req, res);

		
	}
}
