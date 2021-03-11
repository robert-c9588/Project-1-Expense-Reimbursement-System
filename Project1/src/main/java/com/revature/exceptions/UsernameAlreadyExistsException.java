package com.revature.exceptions;

/**
 * This should be thrown when trying to add another user already in the database
 */
public class UsernameAlreadyExistsException extends RuntimeException{
	private static final long serialVersionUID = 1712843686624302051L;
	public UsernameAlreadyExistsException(String message) {
		super(message);
	}

}
