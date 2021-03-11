package com.revature.exceptions;

/**
 * Should be thrown when username and password are invalid.
 */

public class InvalidCredentialsException extends RuntimeException{

	private static final long serialVersionUID = -6573307333524845568L;
	public InvalidCredentialsException(String message) {
		super(message);
	}

}
