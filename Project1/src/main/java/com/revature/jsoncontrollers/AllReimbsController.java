package com.revature.jsoncontrollers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Reimbursement;
import com.revature.dao.ReimbursementDaoDB;
import com.revature.dao.ReimbursmentDao;

public class AllReimbsController {

	public static void getReimbs(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException{
		ReimbursmentDao rDao = new ReimbursementDaoDB();
		
		res.setContentType("application/json");
		List<Reimbursement> list = new ArrayList<Reimbursement>();
		list =  rDao.getAllReimbursements();
		String validjson = new ObjectMapper().writeValueAsString(list);
		System.out.println(validjson);
		res.getWriter().write(validjson);
	}
}
