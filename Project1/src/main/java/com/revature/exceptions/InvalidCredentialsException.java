package com.revature.exceptions;

/**
 * Should be thrown when username and password are invalid.
 */

public class InvalidCredentialsException extends RuntimeException{

	public InvalidCredentialsException(String message) {
		super(message);
	}

}
