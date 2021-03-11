package com.revature.exceptions;

public class InvalidUserSettingsException extends RuntimeException {

	private static final long serialVersionUID = 2051768592689335216L;

	public InvalidUserSettingsException(String message) {
		super(message);
	}
}
