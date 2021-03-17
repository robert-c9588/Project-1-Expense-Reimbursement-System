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
		
		/*
		 * Checking the URI to see where we need to send the request. 
		 */
		if (req.getRequestURI().endsWith(".json")) {
			System.out.println("received get json");
			RequestHelper.process(req, res);
		} else {
			System.out.println("received get rsys");

			req.getRequestDispatcher(RequestHelper.process(req)).forward(req, res);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		System.out.println("in doPost");
		if (req.getRequestURI().endsWith(".json")) {
			System.out.println("received post json");

			RequestHelper.process(req, res);
		} else {
			System.out.println("received get rsys");
			req.getRequestDispatcher(RequestHelper.process(req)).forward(req, res);
		}
	}
	
}
