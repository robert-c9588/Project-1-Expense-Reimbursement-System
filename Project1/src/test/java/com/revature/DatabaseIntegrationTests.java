package com.revature;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
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

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		testUser.setId(1);
		testUser.setUsername("test");
		testUser.setPassword("pass");
		testUser.setFname("test1");
		testUser.setLname("tester");
		testUser.setEmail("test@email.com");
		testUser.setRoleId(UserRole.EMPLOYEE);
		testMan.setId(2);
		testMan.setUsername("mantest");
		testMan.setPassword("manpass");
		testMan.setFname("test1man");
		testMan.setLname("testerman");
		testMan.setEmail("testman@email.com");
		testMan.setRoleId(UserRole.FINANCE_MANAGER);
		udao.addUser(testUser);
		udao.addUser(testMan);
		testReimb.setId(1);
		testReimb.setAmount(28109.18);
		testReimb.setAuthor(testUser);
		testReimb.setResolver(null);
		testReimb.setDescription("For bread.");
		testReimb.setSubmittedTs(LocalDateTime.now());
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
		secondUser.setId(2);
		secondUser.setUsername("test2");
		secondUser.setPassword("pass");
		secondUser.setRoleId(UserRole.EMPLOYEE);
		secondUser.setEmail("test2@email.com");
		udao.addUser(secondUser);
		boolean success =  udao.removeUser(secondUser);
		if(success) {
			assertEquals(1, udao.getAllUsers().size());
		} else {
			fail("unable to delete account");
		}
	}
	
	@Test (expected = UsernameAlreadyExistsException.class)
	public void testUserAlreadyExist()  {
		User secondUser = new User();
		secondUser.setId(1);
		secondUser.setUsername("test");
		secondUser.setPassword("pass");
		secondUser.setRoleId(UserRole.EMPLOYEE);
		secondUser.setEmail("test@email.com");
		udao.addUser(secondUser);

		List<User> ulist = udao.getAllUsers();
		assertEquals(1, ulist.size());
		udao.removeUser(secondUser);
	}
	
	@Test
	public void testGetAllUsers() {
		User secondUser = new User();
		secondUser.setId(2);
		secondUser.setUsername("test2");
		secondUser.setPassword("pass");
		secondUser.setRoleId(UserRole.EMPLOYEE);
		secondUser.setEmail("test2@email.com");
		udao.addUser(secondUser);
		User thirdUser = new User();
		thirdUser.setId(3);
		thirdUser.setUsername("test3");
		thirdUser.setPassword("pass");
		thirdUser.setRoleId(UserRole.FINANCE_MANAGER);
		thirdUser.setEmail("test3@email.com");
		udao.addUser(thirdUser);
		List<User> ulist = udao.getAllUsers();
		assertEquals(3, ulist.size());
		udao.removeUser(secondUser);
		udao.removeUser(thirdUser);
		
	}
	
	//Testing reimbursement class with calls to db.
	

}
