package com.mpay.ccd.exception;

public class RenameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RenameException() {
	}

	public RenameException(String message) {
		super(message);
	}

	public RenameException(Throwable cause) {
		super(cause);
	}

	public RenameException(String message, Throwable cause) {
		super(message, cause);
	}

	public RenameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
