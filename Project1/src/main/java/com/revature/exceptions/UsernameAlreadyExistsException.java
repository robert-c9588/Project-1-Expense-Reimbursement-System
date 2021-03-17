package com.revature.exceptions;

/**
 * This should be thrown when trying to add another user already in the database
 */
public class UsernameAlreadyExistsException extends RuntimeException{

	public UsernameAlreadyExistsException(String message) {
		super(message);
	}

}
