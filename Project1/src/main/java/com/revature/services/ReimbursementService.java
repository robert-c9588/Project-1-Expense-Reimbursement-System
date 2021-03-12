package com.revature.services;

import java.time.LocalDateTime;

import com.revature.beans.Reimbursement;
import com.revature.beans.User;
import com.revature.dao.ReimbursmentDao;
import com.revature.driver.ReimbursementSystemDriver;
import com.revature.exceptions.InvalidArgumentsReimbursementException;

public class ReimbursementService {
	public ReimbursmentDao rdao;

	public ReimbursementService(ReimbursmentDao dao) {
		this.rdao = dao;
	}

	public Reimbursement createNewReimbursement(Reimbursement reimb) {
		Reimbursement r = new Reimbursement();
		try {
			r = rdao.addReimbursment(r);
		} catch (InvalidArgumentsReimbursementException e) {
			r = null;
			ReimbursementSystemDriver.logger.error("Unable to add reimbursement");
			ReimbursementSystemDriver.logger.debug("Reimbursment not written to persistence layer", e);
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
			ReimbursementSystemDriver.logger.error("Unable to udpate status on reimbursement");
			ReimbursementSystemDriver.logger.debug("Reimbursment not written to persistence layer", e);
		}
	}

}