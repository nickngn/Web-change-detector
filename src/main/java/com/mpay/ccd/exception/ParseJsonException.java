package com.mpay.ccd.exception;

public class ParseJsonException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParseJsonException() {
		super();
	}

	public ParseJsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ParseJsonException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseJsonException(String message) {
		super(message);
	}

	public ParseJsonException(Throwable cause) {
		super(cause);
	}

	
}
