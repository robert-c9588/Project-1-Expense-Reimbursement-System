package com.revature.exceptions;

/**
 * This should be thrown when a user tries to access a method not allowed for them
 */

public class UnauthorizedException extends RuntimeException{
	private static final long serialVersionUID = -5565959076866743040L;
	
	public UnauthorizedException(String message) {
		super(message);
	}

}
