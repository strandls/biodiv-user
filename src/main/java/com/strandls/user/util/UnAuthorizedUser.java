package com.strandls.user.util;

public class UnAuthorizedUser extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2194814386087097177L;
	
	public UnAuthorizedUser(String string) {
		super(string);
	}
	
}
