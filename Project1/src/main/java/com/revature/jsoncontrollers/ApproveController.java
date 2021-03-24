package com.revature.jsoncontrollers;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.beans.Reimbursement;
import com.revature.beans.User;
import com.revature.beans.Reimbursement.ReimbStatus;
import com.revature.dao.ReimbursementDaoDB;
import com.revature.dao.ReimbursmentDao;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;

public class ApproveController {
	public static void approve(HttpServletRequest req, HttpServletResponse res){
		ReimbursmentDao rdao = new ReimbursementDaoDB();
		UserDao udao = new UserDaoDB();

		Integer id =  Integer.parseInt(req.getParameter("id"));
		System.out.println("approve: "+ id);

		User manager = udao.getUser((Integer)req.getSession().getAttribute("loggedId"));

		Reimbursement r = rdao.getReimbursement(id);
		r.setResolver(manager);
		r.setResolvedTs(LocalDateTime.now().withNano(0));
		r.setStatusid(ReimbStatus.APPROVED);
		rdao.updateReimbursement(r);
		
	}

}
