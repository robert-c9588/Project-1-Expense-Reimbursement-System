package com.revature;

import static org.junit.Assert.*;

import org.apache.log4j.xml.Log4jEntityResolver;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.beans.Reimbursement;
import com.revature.beans.User;
import com.revature.beans.User.UserRole;
import com.revature.dao.ReimbursementDaoDB;
import com.revature.dao.ReimbursmentDao;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;
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
		testUser =  us.login(testMan.getUsername(), testMan.getPassword());
		us.register(testMan);
	}
}
