package com.revature.services;

import org.apache.log4j.Logger;

import com.revature.beans.User;
import com.revature.dao.ReimbursmentDao;
import com.revature.dao.UserDao;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.InvalidUserSettingsException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.utils.SessionCache;

/**
 * This class contains business logic for performing operations on users
 */
public class UserService {
	//private static final Logger logger = Logger.getLogger(UserService.class);

	public SessionCache cache;
	UserDao userDao;
	ReimbursmentDao reimbDao;

	public UserService(UserDao udao, ReimbursmentDao rdao) {
		this.userDao = udao;
		this.reimbDao = rdao;
	}

	/**
	 * Validates a username and password, and returns the User obj for that user
	 * 
	 * @param uname    Username that is currently trying to login
	 * @param password of that user
	 * @return the User that is now logged in
	 */
	public User login(String uname, String password) {
		User loggedIn = null;
		try {
			loggedIn = userDao.getUser(uname, password);
			SessionCache.setCurrentUser(loggedIn);

		} catch (InvalidCredentialsException e) {
		//	logger.error("Login failed");
		//	logger.debug("User: '" + uname + "'", e);
			return null;

		}
	//	logger.info("User successfully logged in.");
		return loggedIn;
	}

	/**
	 * Creates the specified User in the persistence layer
	 * 
	 * @param newUser the User to be registered
	 * 
	 */
	public void register(User newUser) {
		try {
			userDao.addUser(newUser);
		} catch (UsernameAlreadyExistsException e) {
		//	logger.error("Register failed");
		//	logger.debug("Unable to register new user: '" + newUser.getUsername() + "'", e);
			throw e;
		} catch (InvalidUserSettingsException e) {
		//	logger.error("Register failed");
		//	logger.debug("Unable to register new user: '" + newUser.getUsername() + "'", e);
			throw e;
		}
	//	logger.info("Registered new user");
	}

}
