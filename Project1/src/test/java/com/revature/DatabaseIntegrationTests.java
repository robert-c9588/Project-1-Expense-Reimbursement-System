package com.revature;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
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
import com.revature.exceptions.UsernameAlreadyExistsException;

public class DatabaseIntegrationTests {
	
	 UserDao udao = new UserDaoDB();
	 ReimbursmentDao rdao = new ReimbursementDaoDB();
	 User testUser =  new User();
	 User testMan = new User();
	 Reimbursement testReimb = new Reimbursement();
	 
	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSS");

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
		udao.addUser(testUser);
		udao.addUser(testMan);
		testReimb.setAmount(28109.18);
		testUser = udao.getUser(testUser.getUsername(), testUser.getPassword());
		testMan =udao.getUser(testMan.getUsername(), testMan.getPassword());
		testReimb.setAuthor(testUser);
		testReimb.setResolver(null);
		testReimb.setDescription("For bread.");
		testReimb.setSubmittedTs(LocalDateTime.now().withNano(0));
		testReimb.setResolvedTs(null);
		testReimb.setStatusid(ReimbStatus.PENDING);
		testReimb.setType(ReimbType.FOOD);
		rdao.addReimbursment(testReimb);
	}

	@After
	public void tearDown() throws Exception {
		udao.removeUser(testUser);
		udao.removeUser(testMan);
	}
	//Testing ReimbursmentDaoDB
	@Test
	public void testAddReimbursement() {
		Reimbursement actual = rdao.getReimbursement(testReimb.getId());
		assertEquals(testReimb, actual);
	}
	
	@Test
	public void testGetAllReimbursements() {
		Reimbursement secondReimb = new Reimbursement();
		secondReimb.setAmount(28109.18);
		secondReimb.setAuthor(testUser);
		secondReimb.setResolver(null);
		secondReimb.setDescription("For bread.");
		secondReimb.setSubmittedTs(LocalDateTime.now().withNano(0));
		secondReimb.setResolvedTs(null);
		secondReimb.setStatusid(ReimbStatus.PENDING);
		secondReimb.setType(ReimbType.FOOD);
		
	}
	

	//Testing UserDaoDB
	@Test
	public void testAddUser() {
		User actual = udao.getUser(testUser.getId());
		assertEquals(testUser, actual);
	}
	
	@Test
	public void testLogin() {
		User actual = udao.getUser(testUser.getUsername(),testUser.getPassword());
		assertEquals(testUser, actual);		
	}
	
	@Test
	public void testGetUser() {
		User actual =  udao.getUser(testUser.getId());
		assertEquals(testUser, actual);
	}
	
	@Test
	public void testUpdateUser() {
		testUser.setFname("NewTest");
		testUser.setLname("TestNewLast");
		testUser.setEmail("newemail@test.com");
		udao.updateUser(testUser);
		
		User actual = udao.getUser(testUser.getId());
		
		assertEquals(testUser, actual);
	}
	
	@Test
	public void testDeleteUser() {
		User secondUser = new User();
		secondUser.setUsername("test3");
		secondUser.setPassword("pass3");
		secondUser.setRoleId(UserRole.EMPLOYEE);
		secondUser.setEmail("test3@email.com");
		secondUser = udao.addUser(secondUser);
		boolean success =  udao.removeUser(secondUser);
		if(success) {
			assertEquals(2, udao.getAllUsers().size());
		} else {
			fail("unable to delete account");
		}
	}
	
	@Test (expected = UsernameAlreadyExistsException.class)
	public void testUserAlreadyExist()  {
		User secondUser = new User();
		secondUser.setUsername("test");
		secondUser.setPassword("pass");
		secondUser.setRoleId(UserRole.EMPLOYEE);
		secondUser.setEmail("test@email.com");
		secondUser = udao.addUser(secondUser);

		List<User> ulist = udao.getAllUsers();
		assertEquals(1, ulist.size());
		udao.removeUser(secondUser);
	}
	
	@Test
	public void testGetAllUsers() {
		User thirdUser = new User();
		thirdUser.setUsername("test4");
		thirdUser.setPassword("pass");
		thirdUser.setRoleId(UserRole.EMPLOYEE);
		thirdUser.setEmail("test4@email.com");
		thirdUser = udao.addUser(thirdUser);
		User fourthUser = new User();
		fourthUser.setUsername("test5");
		fourthUser.setPassword("pass");
		fourthUser.setRoleId(UserRole.FINANCE_MANAGER);
		fourthUser.setEmail("test5@email.com");
		fourthUser = udao.addUser(fourthUser);
		List<User> ulist = udao.getAllUsers();
		assertEquals(4, ulist.size());
		udao.removeUser(thirdUser);
		udao.removeUser(fourthUser);
		
	}
	
	//Testing reimbursement class with calls to db.
	

}
