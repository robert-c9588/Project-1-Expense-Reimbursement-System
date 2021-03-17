package com.revature.exceptions;

/**
 * This should be thrown when a user tries to access a method not allowed for them
 */

public class UnauthorizedException extends RuntimeException{
	
	public UnauthorizedException(String message) {
		super(message);
	}

}
