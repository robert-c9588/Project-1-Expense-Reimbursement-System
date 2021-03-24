package com.revature.services;

import java.time.LocalDateTime;

import org.apache.log4j.Logger;

import com.revature.beans.Reimbursement;
import com.revature.beans.User;
import com.revature.dao.ReimbursmentDao;
import com.revature.exceptions.InvalidArgumentsReimbursementException;

public class ReimbursementService {
	//private static final Logger logger = Logger.getLogger(ReimbursementService.class);

	public ReimbursmentDao rdao;

	public ReimbursementService(ReimbursmentDao dao) {
		this.rdao = dao;
	}

	public Reimbursement createNewReimbursement(Reimbursement reimb) {
		Reimbursement r = reimb;
		System.out.println("in reimbservice");
		System.out.println(r);
		try {
			r = rdao.addReimbursment(r);
		} catch (InvalidArgumentsReimbursementException e) {
			r = null;
		//	logger.error("Unable to add reimbursement");
		//	logger.debug("Reimbursment not written to persistence layer", e);
		}
		return r;
	}

	public void approveOrDenyReimbursement(Reimbursement reimb, Reimbursement.ReimbStatus reimbstat, User user) {
		Reimbursement r = reimb;
		LocalDateTime ldt = LocalDateTime.now().withNano(0);
		r.setStatusid(reimbstat);
		r.setResolvedTs(ldt);
		r.setResolver(user);

		try {
			rdao.updateReimbursement(r);
		} catch (InvalidArgumentsReimbursementException e) {
		//	logger.error("Unable to udpate status on reimbursement");
		//	logger.debug("Reimbursment not written to persistence layer", e);
		}
	}

}