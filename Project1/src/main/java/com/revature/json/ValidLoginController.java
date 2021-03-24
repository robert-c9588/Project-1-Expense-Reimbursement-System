package com.revature.json;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ValidLoginController {

	public static void check(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException{
		Integer s = (Integer) req.getSession().getAttribute("loggedId");
		res.setContentType("application/json");

		if (s>0) {

			res.getWriter().write(new ObjectMapper().writeValueAsString("true"));

		} else {

			res.getWriter().write(new ObjectMapper().writeValueAsString("false"));
		}
	}
}
