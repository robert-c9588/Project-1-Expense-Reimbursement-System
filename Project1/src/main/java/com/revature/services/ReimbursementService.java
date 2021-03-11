package com.revature.services;

import com.revature.beans.Reimbursement;
import com.revature.beans.User;
import com.revature.dao.ReimbursmentDao;

public class ReimbursementService {
	public ReimbursmentDao rdao;
	
	public ReimbursementService(ReimbursmentDao dao) {
		this.rdao = dao;
	}
	
	public Reimbursement createNewReimbursement(User u) {
		
		return null;
	}
	
	public boolean approveOrDenyReimbursement(Reimbursement reimb, boolean approval) {
		
		return false;
	}
	
	
}