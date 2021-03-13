package com.revature.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JSONMasterServlet extends HttpServlet {

	private static final long serialVersionUID = 4231116946943193981L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException{
		System.out.println("in JSON doGet");
		JSONRequestHelper.process(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException{
		System.out.println("in JSON doPOST");
		JSONRequestHelper.process(req, res);
	}
}
