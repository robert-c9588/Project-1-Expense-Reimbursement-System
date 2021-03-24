package com.revature;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.log4j.xml.Log4jEntityResolver;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo.AssertionFailedException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.beans.Reimbursement;
import com.revature.beans.Reimbursement.ReimbStatus;
import com.revature.beans.Reimbursement.ReimbType;
import com.revature.beans.User;
import com.revature.beans.User.UserRole;
import com.revature.dao.ReimbursementDaoDB;
import com.revature.dao.ReimbursmentDao;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;
import com.revature.exceptions.InvalidUserSettingsException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;

public class ServicesTests {

	UserDao udao = new UserDaoDB();
	ReimbursmentDao rdao = new ReimbursementDaoDB();
	UserService us=  new UserService(udao, rdao);
	ReimbursementService rs =  new ReimbursementService(rdao);
	User testUser = new User();
	User testMan = new User();
	Reimbursement testReimb = new Reimbursement();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		testUser.setUsername("test");
		testUser.setPassword("pass");
		testUser.setFname("test1");
		testUser.setLname("tester");
		testUser.setEmail("test@email.com");
		testUser.setRoleId(UserRole.EMPLOYEE);
		testMan.setUsername("mantest");
		testMan.setPassword("manpass");
		testMan.setFname("test1man");
		testMan.setLname("testerman");
		testMan.setEmail("testman@email.com");
		testMan.setRoleId(UserRole.FINANCE_MANAGER);
		testReimb.setAmount(1.00);
		testReimb.setDescription("candy");
		testReimb.setSubmittedTs(LocalDateTime.now().withNano(0));
		testReimb.setType(ReimbType.FOOD);
		testReimb.setStatusid(ReimbStatus.PENDING);
	}

	@After
	public void tearDown() throws Exception {
		udao.removeUser(testUser);
		udao.removeUser(testMan);
	}

	@Test
	public void testRegisterAndLoginUser() {
		us.register(testUser);
		us.register(testMan);
		testUser =  us.login(testUser.getUsername(), testUser.getPassword());
		testMan =  us.login(testMan.getUsername(), testMan.getPassword());
	}
	
	@Test(expected=UsernameAlreadyExistsException.class)
	public void testInvalidRegistration() {
		us.register(testMan);
		us.register(testUser);
		this.testUser = us.login(testUser.getUsername(), testUser.getPassword());
		this.testMan =  us.login(testMan.getUsername(), testMan.getPassword());
		us.register(this.testMan);
	}
	@Test(expected=InvalidUserSettingsException.class)
	public void testInvalidSettingsRegistration() {
		us.register(testMan);
		us.register(testUser);
		
		User u = new User();
		u.setPassword("pass");
		u.setFname("test1");
		u.setLname("tester");
		u.setRoleId(UserRole.EMPLOYEE);
		this.testUser = us.login(testUser.getUsername(), testUser.getPassword());
		this.testMan =  us.login(testMan.getUsername(), testMan.getPassword());
		us.register(u);
	}
	
	//Reimbursement Services test
	@Test
	public void testAddReimb() {
		us.register(testMan);
		us.register(testUser);
		this.testUser = us.login(testUser.getUsername(), testUser.getPassword());
		this.testMan =  us.login(testMan.getUsername(), testMan.getPassword());
		
		testReimb.setAuthor(testUser);
		testReimb.setResolver(testMan);
		

		rs.createNewReimbursement(testReimb);
			
		List<Reimbursement> rlist = rdao.getReimbursementsByEmployee(testUser);
		Reimbursement r = rlist.get(0);
		
		assertEquals(r, rdao.getReimbursement(r.getId()));
	}
	
	@Test
	public void testApproveDenyReimb() {
		us.register(testMan);
		us.register(testUser);
		this.testUser = us.login(testUser.getUsername(), testUser.getPassword());
		this.testMan =  us.login(testMan.getUsername(), testMan.getPassword());
		
		testReimb.setAuthor(testUser);
		testReimb.setResolver(testMan);
		
		rs.createNewReimbursement(testReimb);
		rs.createNewReimbursement(testReimb);
		List<Reimbursement> rlist = rdao.getReimbursementsByEmployee(testUser);
		Reimbursement r1 = rlist.get(0);
		Reimbursement r2 = rlist.get(1);
		
		rs.approveOrDenyReimbursement(r1, ReimbStatus.APPROVED, testMan);
		rs.approveOrDenyReimbursement(r2, ReimbStatus.DENIED, testMan);
		
		assertEquals(r1, rdao.getReimbursement(r1.getId()));
		assertEquals(r2, rdao.getReimbursement(r2.getId()));
		
	}
}
